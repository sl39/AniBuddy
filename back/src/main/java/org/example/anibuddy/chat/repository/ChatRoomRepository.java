package org.example.anibuddy.chat.repository;

import org.example.anibuddy.chat.model.ChatRoomEntity;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Integer> {

    Optional<ChatRoomEntity> findById(int roomId);

    Optional<ChatRoomEntity> findByUserAndOwner(UserEntity user, OwnerEntity owner);

    // @Query("select c from ChatRoomEntity c where c.user.id = :#{#userId}")
    List<ChatRoomEntity> findAllByUser(UserEntity user);

    List<ChatRoomEntity> findAllByOwner(OwnerEntity owner);
}
