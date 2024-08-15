package org.example.anibuddy.auth.service;

import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.user.Role;
import org.example.anibuddy.user.UserEntity;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final AuthRepository authRepository;


    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity authEntity = authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email not found"));



        return CustomUserDetails.builder()
                .userId(authEntity.getId())
                .username(authEntity.getEmail())
                .password(authEntity.getPassword())
                .role(authEntity.getRole().getKey())
                .build();
    }
}
