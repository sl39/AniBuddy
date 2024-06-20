package org.example.anibuddy.global.websocket;

import jakarta.persistence.*;
import org.example.anibuddy.owner.OwnerEntity;

@Entity
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoomEntity chatRoom;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String role;
}
