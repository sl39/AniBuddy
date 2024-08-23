package org.example.anibuddy.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.dto.ChatRoomRequest;
import org.example.anibuddy.chat.dto.ChatRoomResponse;
import org.example.anibuddy.chat.service.ChatRoomService;
import org.example.anibuddy.global.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        int id = principal.getUserId();

        return ResponseEntity.ok(chatRoomService.makeChatRoom(id, chatRoomRequest));
    }

    @GetMapping("/chatrooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomList() {
        //Access 토큰 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        int id = principal.getUserId();
        String role = principal.getRole();
        Role roleEnum = Role.valueOf(role);
        return ResponseEntity.ok(chatRoomService.getChatRoomList(roleEnum, id));
    }
}
