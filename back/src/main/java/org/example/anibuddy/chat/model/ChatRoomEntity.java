package org.example.anibuddy.chat.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;

@NoArgsConstructor
@Getter
@Entity
@Table(name="chat_room_entity")
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;

    @Builder
    public ChatRoomEntity(UserEntity user, OwnerEntity owner) {
        this.user=user;
        this.owner=owner;
    }
}
