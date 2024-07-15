package org.example.anibuddy.global.jwt.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.global.jwt.service.JwtService;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.global.jwt.util.PasswordUtil;
import org.example.anibuddy.user.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * JWT 인증 필터
 * "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 * 기본적으로 사용자는 요청 헤더에 AccessToken 만 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AcessToken과 함께 요청
 *
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 2. RefreshToekn이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 3. RefreshToeken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급 (RTR 방식)
 * 인증 성공 처리는 하지 않고 실패 처리
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String NO_CHECK_URL = "/api/auth/login"; // /api/auth/login 으로 들어오는 요청은 filter 작동X

    private final JwtService jwtService;
    private final AuthRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // /api/auth/login 요청이 들어오면, 다른 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행 시킴)
        }

        // 사용자 요청 헤더에서 RefreshToken 추출
        // -> RefreshToekn이 없거나 유효하지 않다면(DB에 저장된 RefreshToken과 다르다면) null을 반환
        // 사용자의 요청 헤더에 RefreshToken에 있는 경우는, AeccessToken이 만료되어 요청한 경우밖에 없다.
        // 따라서, 위의 경우를 제외하면 추출한 refreshToken은 모두 null
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // RefreshToken이 요쳥 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
        // RefreshToken까지 보낸 것이므로 리프레시 토큰이 DB이 리프레시 토큰과 일치하는지 판단 후,
        // 일치한다면 AccessToken을 재발급해준다.
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response,refreshToken);
            return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기 위해 바로 return으로 필터 진행 막기
        }


        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담기 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if(refreshToken == null){
            checkAccessToeknAndAuthentication(request,response,filterChain);
        }

    }

    // refreshToken으로 유저 정보 찾기 & 엑세스 토큰/리프레시 토큰 재발급 메소드
    // 파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있다면,
    // JwtService.createAccessToken()으로 AccessToken 생성,
    // reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
    // 그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(authEntity -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(authEntity);
                    try {
                        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(authEntity.getEmail()),
                                reIssuedRefreshToken);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    // 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드
    // jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
    // DB에 재발급한 리프레시 토큰 업데이트 후 Flush
    private String reIssueRefreshToken(UserEntity authEntity) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        authEntity.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(authEntity);
        return reIssuedRefreshToken;
    }


    // 엑세스 토큰 체크 & 인증 처리 메소드
    // request에서 extractAccessToken()으로 엑세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
    // 유효한 토큰이면, 엑세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
    // 그 유저 객체를 saveAuthentication()으로 인증 처리
    // 인증 허가 처리된 객체를 SecurityContextHolder에 담기
    // 그 후 다음 인증필터로 진행
    private void checkAccessToeknAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessToeknAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));
        filterChain.doFilter(request,response);


    }

    /**
     * [인증 허가 메소드]
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     *
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터
     * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
     * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
     * 3. Collection < ? extends GrantedAuthority>로,
     * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
     * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(UserEntity myUser) {
        String password = myUser.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
            System.out.println(password);
        }

//         유저의 역할, 즉 관리자인지, 게스트인지등등
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getRole().name())
                .build();
//        CustomUserDetails userDetailsUser = CustomUserDetails.builder()
//                .username(myUser.getEmail())
//                .userId(myUser.getId())
//                .password(password)
//                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
