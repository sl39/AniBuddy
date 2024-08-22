package org.example.anibuddy.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ChatRoomResponse {

    private int roomId;
    private String otherName;
    private String otherProfileImageUrl;
}
