package org.example.anibuddy.global.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class SocketHandler extends TextWebSocketHandler {

    List<HashMap<String, Object>> sessionList = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        boolean flag = false;
        String url = session.getUri().toString();
        String roomNumber = url.split("/chat/")[1];

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(sessionList.size() > 0) {
            for(int i=0; i<sessionList.size(); i++) {
                sessionList.get(i).remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }
}
