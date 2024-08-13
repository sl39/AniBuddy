package org.example.anibuddy.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.anibuddy.auth.Dto.AuthSignUpRequestDto;
import org.example.anibuddy.auth.AuthRepository;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.owner.OwnerRepository;
import org.example.anibuddy.user.Role;
import org.example.anibuddy.user.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignupService {

    private final AuthRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;

    public Boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public Boolean checkNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    @Transactional
    public ResponseEntity<?> signup(AuthSignUpRequestDto authSignUpRequestDto) throws Exception {
        if(!authSignUpRequestDto.getPassword().equals(authSignUpRequestDto.getPassword2())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(userRepository.findByEmail(authSignUpRequestDto.getEmail()).isPresent()){
            log.info("이메일 중복입니다");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(userRepository.findByNickname(authSignUpRequestDto.getNickname()).isPresent()){
            log.info("닉네임 중복입니다");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                 .email(authSignUpRequestDto.getEmail())
                .password(authSignUpRequestDto.getPassword())
                .userAddress(authSignUpRequestDto.getUserAddress())
                .userName(authSignUpRequestDto.getUserName())
                .nickname(authSignUpRequestDto.getNickname())
                .role(Role.valueOf(authSignUpRequestDto.getRole()))
                .build();


        user.passwordEncode(passwordEncoder);
        Map<String,String> map = new HashMap<>();
        userRepository.save(user);
        if(user.getRole().getKey().equals("ROLE_OWNER")){
            OwnerEntity ownerEntity = OwnerEntity.builder()
                    .id(user.getId())
                    .name(user.getUserName())
                    .email(user.getEmail())
                    .build();
            ownerRepository.save(ownerEntity);
        }
        map.put("email", user.getEmail());
        ResponseEntity response = new ResponseEntity<>(map,HttpStatus.OK);
        return response;
    }

    public ResponseEntity signupAll(List<AuthSignUpRequestDto> authSignUpRequestDtoList) throws Exception {
        List<UserEntity> users = new ArrayList<>();
        for(AuthSignUpRequestDto authSignUpRequestDto : authSignUpRequestDtoList){
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
                    .userName(authSignUpRequestDto.getUserName())
                    .nickname(authSignUpRequestDto.getNickname())
                    .role(Role.USER)
                    .build();

            user.passwordEncode(passwordEncoder);
            users.add(user);

        }

        userRepository.saveAll(users);
        ResponseEntity response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }
}
