package org.example.anibuddy.chat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageEntity {

    private int id;
    private int chatRoomId;
    private Role role;
    private int chatterId;

    private String content;
    private LocalDateTime createdAt;
    private Status status;
}
