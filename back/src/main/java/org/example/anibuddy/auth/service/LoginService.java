package org.example.anibuddy.auth.service;

import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.user.UserEntity;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final AuthRepository authRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity authEntity = authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(authEntity.getEmail())
                .password(authEntity.getPassword())
                .build();
    }
}
