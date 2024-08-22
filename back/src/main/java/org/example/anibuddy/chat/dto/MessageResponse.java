package org.example.anibuddy.chat.dto;

import java.time.LocalDateTime;

public class MessageResponse {

    int chatRoomId;
    String content;
    String status;
    String role;
    LocalDateTime createdAt;
}
