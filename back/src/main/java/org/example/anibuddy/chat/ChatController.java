package org.example.anibuddy.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ChatController {

    //채팅방 : 채팅방 단건/리스트 조회, 채팅방 개설 (채팅 메세지 저장은 Websocket이라서 별개로)


}
