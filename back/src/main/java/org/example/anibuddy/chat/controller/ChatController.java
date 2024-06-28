package org.example.anibuddy.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.dto.ChatRoomRequest;
import org.example.anibuddy.chat.dto.ChatRoomResponse;
import org.example.anibuddy.chat.dto.MessageResponse;
import org.example.anibuddy.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chat")
public class ChatController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponse> makeChatRoom(
            @RequestBody ChatRoomRequest chatRoomRequest
    ) {
        //TODO: Spring security의 @Authentication으로 요청한 user의 id값 받아오기
        int userId = 1;

        return ResponseEntity.ok(chatRoomService.makeChatRoom(userId, chatRoomRequest));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomList()
    {
        //TODO: Spring security의 @Authentication으로 요청한 user의 id값 받아오기
        int userId = 1;

        return ResponseEntity.ok(chatRoomService.getChatRoomList(userId));
    }

//    @GetMapping()
//    public ResponseEntity<List<MessageResponse>> getMessageList() {
//        //채팅방 단건 조회
//    }
//
}
