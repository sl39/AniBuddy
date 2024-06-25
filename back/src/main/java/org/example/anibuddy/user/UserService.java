package org.example.anibuddy.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Getter
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUser(String email) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found")));
        return user.get().getUserName();
    }
}
