package org.example.anibuddy.chat.dto;

import lombok.Getter;
import org.example.anibuddy.chat.model.ChatRoomEntity;

@Getter
public class ChatRoomResponse {

    private int roomId;
    private String ownerName;

    public ChatRoomResponse(ChatRoomEntity chatRoomEntity) {
        this.roomId = chatRoomEntity.getId();
        this.ownerName = chatRoomEntity.getOwner().getName();
    }
}
