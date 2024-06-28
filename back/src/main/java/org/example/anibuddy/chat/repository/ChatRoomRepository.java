package org.example.anibuddy.chat.repository;

import org.example.anibuddy.chat.model.ChatRoomEntity;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Integer> {

    Optional<ChatRoomEntity> findById(int roomId);

    Optional<ChatRoomEntity> findByUserAndOwner(UserEntity user, OwnerEntity owner);

    List<ChatRoomEntity> findAllByUser(UserEntity user);
}
