package org.example.anibuddy.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.user.UserDto.UserSignUpRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signup(UserSignUpRequestDto userSignUpRequestDto) throws Exception {
        if(userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일 입니다.");
        }
        UserEntity user = UserEntity.builder()
                 .email(userSignUpRequestDto.getEmail())
                .password(userSignUpRequestDto.getPassword())
                .userAddress(userSignUpRequestDto.getUserAddress())
                .userPhone(userSignUpRequestDto.getUserPhone())
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }
}
