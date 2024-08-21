package org.example.anibuddy.global.websocket;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.anibuddy.chat.model.Role;
import org.example.anibuddy.notification.repository.FcmTokenRepository;
import org.example.anibuddy.notification.service.NotificationService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketHandler extends TextWebSocketHandler {

    private final NotificationService notificationService;
    List<HashMap<String, Object>> chatRoomList = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {

        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);

        Integer reqRoomId = Integer.parseInt((String)obj.get("roomId"));
        String reqSenderName = (String)obj.get("senderName");
        Role reqReceiverRole = Role.valueOf((String)obj.get("receiverRole"));
        Integer reqReceiverId = Integer.parseInt((String)obj.get("receiverId"));

        HashMap<String, Object> tempSessions = new HashMap<String, Object>();
        if(!chatRoomList.isEmpty()) {
            //request의 roodId에 해당하는 세션맵 찾기
            for(int i=0; i<chatRoomList.size(); i++) {
                Integer roomId = (Integer) chatRoomList.get(i).get("roomId");
                if(roomId.equals(reqRoomId)){
                    tempSessions = chatRoomList.get(i);
                    break;
                }
            }
            //해당 방에 있는 세션들에게 메세지 발송하기
            for(String key : tempSessions.keySet()) {
                if(key.equals("roomId")) continue;

                WebSocketSession wsSession = (WebSocketSession) tempSessions.get(key);
                if(wsSession != null) {
                    try {
                        TextMessage textMessage = new TextMessage(msg);
                        wsSession.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
            //상대방의 웹소켓 세션이 없는 경우 FCM 푸시 알림 요청
            if(tempSessions.size() < 3) {
                notificationService.pushChatNotification(
                        reqReceiverId, reqReceiverRole, msg);
            }

        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        boolean isExistRoom = false;
        String url = session.getUri().toString();
        Integer roomId = Integer.parseInt(url.split("/chat/")[1]); // "/chat/" 뒤의 문자열 저장

        //요청한 방 id가 세션맵 리스트에 존재하는지 확인
        int roomIdx = chatRoomList.size();
        if(!chatRoomList.isEmpty()) {
            for(int i=0; i<chatRoomList.size(); i++) {
                int roomNum = (int) chatRoomList.get(i).get("roomId");
                if(roomId.equals(roomNum)) {
                    isExistRoom = true;
                    roomIdx = i;
                    break;
                }
            }
        }

        if(isExistRoom) { //기존에 있는 방이라면 세션 추가
            HashMap<String, Object> map = chatRoomList.get(roomIdx);
            map.put(session.getId(), session);
        }else { //최초 생성하는 방이라면 roomId와 세션 추가
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("roomId", roomId);
            map.put(session.getId(), session);
            chatRoomList.add(map);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(!chatRoomList.isEmpty()) { //소켓이 종료되면 해당 세션값들을 찾아서 지운다.
            for(int i=0; i<chatRoomList.size(); i++) {
                chatRoomList.get(i).remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }

    private JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return obj;
    }
}
