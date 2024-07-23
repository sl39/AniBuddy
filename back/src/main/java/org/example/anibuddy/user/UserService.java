package org.example.anibuddy.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    public String getUser(String email) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found")));
        return user.get().getUserName();
    }
    public UserEntity getUserByNickname(String nickname) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException("email not found")));
        return user.get();
    }

}
