package org.example.anibuddy.global.websocket;

import jakarta.persistence.*;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.user.UserEntity;

@Entity
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
}
