package org.example.anibuddy.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.auth.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import org.example.anibuddy.auth.handler.LoginFailureHandler;
import org.example.anibuddy.auth.handler.LoginSuccessHandler;
import org.example.anibuddy.auth.service.LoginService;
import org.example.anibuddy.global.jwt.filter.JwtAuthenticationProcessingFilter;
import org.example.anibuddy.global.jwt.service.JwtService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final AuthRepository authRepository;
    private final ObjectMapper objectMapper;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                        .formLogin(AbstractHttpConfigurer::disable) // FormLogin 사용X
                .httpBasic(AbstractHttpConfigurer::disable)// httpBasic 사용X
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 사용 X
                .headers(
                        headersConfigurer ->
                                headersConfigurer
                                        .frameOptions(
                                                HeadersConfigurer.FrameOptionsConfig::disable
                                        ))
                // 세션 사용하지 않으므로 STATELESS 로 설정
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // url 별 권한 관리 옵션
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/api/auth/signup").permitAll()
                                .anyRequest().authenticated())// 나머지는 안됨

        ;

        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // AuthenticationManager 설정 후 등록
    // PasswordEncoder를 사용하는 AuthenticationProvider 지정  (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
    // FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
    // UserDetailService는 커스텀 LoginService로 등록
    // 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }


    // 로그인 성공시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(jwtService, authRepository);
    }

    // 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }

    // CustomJsonUesrnamePasswordAuthenticationFilter 빈 등록
    // 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 bean으로 등록
    // setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
    // 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter(){
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter =
                new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper,authRepository);
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordAuthenticationFilter;
    }
    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, authRepository);
        return jwtAuthenticationFilter;
    }
}
