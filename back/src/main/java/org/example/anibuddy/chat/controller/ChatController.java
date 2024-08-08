package org.example.anibuddy.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.dto.ChatRoomRequest;
import org.example.anibuddy.chat.dto.ChatRoomResponse;
import org.example.anibuddy.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.anibuddy.chat.model.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/user/chatrooms")
    public ResponseEntity<ChatRoomResponse> makeChatRoom(
            @RequestBody ChatRoomRequest chatRoomRequest
    ) {
        //TODO: Spring security의 @Authentication으로 요청한 user의 id값 받아오기
        int userId = 1;

        return ResponseEntity.ok(chatRoomService.makeChatRoom(userId, chatRoomRequest));
    }

    @GetMapping("/chatrooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomList(
            @RequestParam("myId") int id,
            @RequestParam("role") String role
    ) {
        Role roleEnum = Role.valueOf(role);
        return ResponseEntity.ok(chatRoomService.getChatRoomList(roleEnum, id));
    }
}
