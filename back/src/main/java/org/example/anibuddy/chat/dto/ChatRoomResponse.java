package org.example.anibuddy.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ChatRoomResponse {

    private int roomId;

    private int myId;
    private String myRole;
    private String myName;
    private String myImageUrl;

    private int otherId;
    private String otherRole;
    private String otherName;
    private String otherImageUrl;
}
