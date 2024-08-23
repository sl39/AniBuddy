package org.example.anibuddy.notification.repository;

import org.example.anibuddy.notification.model.NotificationEntity;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    List<NotificationEntity> findAllByToUser(UserEntity user);

    List<NotificationEntity> findAllByToOwner(OwnerEntity owner);
}