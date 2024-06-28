package org.example.anibuddy.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    private int ownerId;

    @Builder
    public ChatRoomRequest(int ownerId) {
        this.ownerId = ownerId;
    }
}
