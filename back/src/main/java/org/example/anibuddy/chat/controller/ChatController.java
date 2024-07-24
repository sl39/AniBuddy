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

    @GetMapping("/user/chatrooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomListForUser()
    {
        //TODO: Spring security의 @Authentication으로 요청한 user의 id값 받아오기
        int userId = 1;
        Role role = Role.USER;

        return ResponseEntity.ok(chatRoomService.getChatRoomList(role, userId));
    }

    @GetMapping("/owner/chatrooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomListForOwner()
    {
        //TODO: Spring security의 @Authentication으로 요청한 owner의 id값 받아오기
        int ownerId = 2;
        Role role = Role.OWNER;

        return ResponseEntity.ok(chatRoomService.getChatRoomList(role, ownerId));
    }
}
