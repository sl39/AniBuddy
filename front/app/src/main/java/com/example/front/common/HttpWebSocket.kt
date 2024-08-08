package com.example.front.common

import android.util.Log
import com.example.front.data.ChatMessage
import com.example.front.data.Role
import io.realm.Realm
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

public class HttpWebSocket : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("CHATTING", "Socket Open");
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("CHATTING", "수신 데이터 : " + text.toString());

        //TODO: Realm DB 저장
        val db = Realm.getDefaultInstance()
        db.executeTransactionAsync {
            val msg = ChatMessage(
                roomId = 1,
                message = "Realm test msg",
                senderId = 1,
                senderName = "Realm test name",
                senderRole = Role.USER.toString()
            )
            it.insert(msg)
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(1000, null);
        webSocket.cancel();
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
    }
}