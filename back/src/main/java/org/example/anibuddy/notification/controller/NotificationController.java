package org.example.anibuddy.notification.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.dto.ChatRoomResponse;
import org.example.anibuddy.chat.model.Role;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.notification.dto.FcmTokenResponse;
import org.example.anibuddy.notification.dto.NotificationResponse;
import org.example.anibuddy.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    //토큰 등록
    @PostMapping("/fcm")
    public ResponseEntity<FcmTokenResponse> registerFcmToken(@RequestBody String fcmToken) {
        //Access 토큰 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        int id = principal.getUserId();
        String role = principal.getRole();
        Role roleEnum = Role.valueOf(role);
        logger.info("컨트롤러 registerFcmToken 호출");

        return ResponseEntity.ok(notificationService.registerFcmToken(id, roleEnum, fcmToken));
    }

    //알림 리스트 조회
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> getNotificationList() {
        //Access 토큰 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        int id = principal.getUserId();
        String role = principal.getRole();
        Role roleEnum = Role.valueOf(role);
        return ResponseEntity.ok(notificationService.getNotificationList(roleEnum, id));
    }
}