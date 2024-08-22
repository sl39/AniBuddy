package org.example.anibuddy.notification.repository;

import org.example.anibuddy.notification.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {


}