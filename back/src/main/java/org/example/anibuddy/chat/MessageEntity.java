package org.example.anibuddy.chat;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Role role;
}
