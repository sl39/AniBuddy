package org.example.anibuddy.notification.repository;

import org.example.anibuddy.notification.model.FcmTokenEntity;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Integer> {

    Optional<FcmTokenEntity> findByUser(UserEntity user);

    Optional<FcmTokenEntity> findByOwner(OwnerEntity owner);
}
