package org.example.anibuddy.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.auth.Dto.AuthSignUpRequestDto;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.user.Role;
import org.example.anibuddy.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SignupService {

    private final AuthRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signup(AuthSignUpRequestDto authSignUpRequestDto) throws Exception {
        if(!authSignUpRequestDto.getPassword().equals(authSignUpRequestDto.getPassword2())){
            throw new Exception("비밀번호가 맞지 않습니다");
        }

        if(userRepository.findByEmail(authSignUpRequestDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일 입니다.");
        }
        System.out.println(authSignUpRequestDto.getUserName());

        UserEntity user = UserEntity.builder()
                 .email(authSignUpRequestDto.getEmail())
                .password(authSignUpRequestDto.getPassword())
                .userAddress(authSignUpRequestDto.getUserAddress())
                .userPhone(authSignUpRequestDto.getUserPhone())
                .userName(authSignUpRequestDto.getUserName())
                .nickname(authSignUpRequestDto.getNickname())
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }
}
