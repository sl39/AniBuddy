package org.example.anibuddy.global.oauth2.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.global.jwt.service.JwtService;
import org.example.anibuddy.global.oauth2.CustomOAuth2User;
import org.example.anibuddy.user.Role;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.rmi.server.ServerCloneException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 login successful");


        try{
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            if(oAuth2User.getRole() == Role.GUEST){
//                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());

//                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//                jwtService.sendAccessAndRefreshToken(response,accessToken,null);

                UserEntity findUser = userRepository.findByEmail(oAuth2User.getEmail())
                                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
                findUser.authorizeUser();
                userRepository.save(findUser);


            }
            loginSuccess(response,oAuth2User);


        } catch (Exception e){
            throw e;
        }
    }

    private void loginSuccess(final HttpServletResponse response, final CustomOAuth2User oAuth2User) throws IOException{
        String email = oAuth2User.getEmail();
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답

        authRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.updateRefreshToken(refreshToken);
                    authRepository.saveAndFlush(user);
                });
    }
}
