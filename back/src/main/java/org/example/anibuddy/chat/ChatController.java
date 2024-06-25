package org.example.anibuddy.chat;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.dto.ChatRoomResponse;
import org.example.anibuddy.chat.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ChatController {

    @GetMapping()
    public ResponseEntity<List<MessageResponse>> getMessageList() {
        //채팅방 단건 조회
    }

    @GetMapping()
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomList() {
        //채팅방 리스트 조회
    }

    @PostMapping()
    public ResponseEntity<ChatRoomResponse> createChatRoom() {
        //채팅방 생성
        return ResponseEntity
    }
}
