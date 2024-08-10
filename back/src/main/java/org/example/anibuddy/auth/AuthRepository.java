package org.example.anibuddy.auth;

import org.apache.catalina.User;
import org.example.anibuddy.user.Role;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByRefreshToken(String refreshToken);

    Optional<UserEntity> findByEmailAndRole(String username, Role role);

    Optional<UserEntity> findByNickname(String nickname);

}
