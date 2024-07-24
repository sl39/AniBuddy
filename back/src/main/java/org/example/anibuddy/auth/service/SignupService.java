package org.example.anibuddy.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.anibuddy.auth.Dto.AuthSignUpRequestDto;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.user.Role;
import org.example.anibuddy.user.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignupService {

    private final AuthRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> signup(AuthSignUpRequestDto authSignUpRequestDto) throws Exception {
        if(!authSignUpRequestDto.getPassword().equals(authSignUpRequestDto.getPassword2())){
            throw new Exception("비밀번호가 맞지 않습니다");
        }

        if(userRepository.findByEmail(authSignUpRequestDto.getEmail()).isPresent()){
            log.info("이메일 중복입니다");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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
        ResponseEntity response = new ResponseEntity<>(user.getEmail(),HttpStatus.OK);
        return response;
    }
}
