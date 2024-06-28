package org.example.anibuddy.chat.dto;

import lombok.Getter;
import org.example.anibuddy.chat.model.ChatRoomEntity;

@Getter
public class ChatRoomResponse {

    private final int roomId;
    private final String ownerName;
    private final String userName;

    public ChatRoomResponse(ChatRoomEntity chatRoomEntity) {
        this.roomId = chatRoomEntity.getId();
        this.ownerName = chatRoomEntity.getOwner().getName();
        this.userName = chatRoomEntity.getUser().getUserName();
    }
}
