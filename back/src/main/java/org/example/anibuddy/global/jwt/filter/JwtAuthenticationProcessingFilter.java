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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String NO_CHECK_URL = "/api/auth/login"; // /api/auth/login 으로 들어오는 요청은 filter 작동X

    private final JwtService jwtService;
    private final AuthRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // /api/auth/login 요청이 들어오면, 다른 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행 시킴)
        }

        // 사용자 요청 헤더에서 RefreshToken 추출
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        if (refreshToken == null) {
            checkAccessToeknAndAuthentication(request, response, filterChain);
        }
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresentOrElse(authEntity -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(authEntity);
                    try {
                        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(authEntity.getEmail()),
                                reIssuedRefreshToken);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, () -> {
                    // RefreshToken is invalid
                    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                });
    }

    private String reIssueRefreshToken(UserEntity authEntity) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        authEntity.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(authEntity);
        return reIssuedRefreshToken;
    }

    private void checkAccessToeknAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessToeknAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));
        filterChain.doFilter(request,response);
    }

    public void saveAuthentication(UserEntity myUser) {
        String password = myUser.getPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
            System.out.println(password);
        }

        CustomUserDetails userDetailsUser = CustomUserDetails.builder()
                .username(myUser.getEmail())
                .password(password)
                .userId(myUser.getId())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
