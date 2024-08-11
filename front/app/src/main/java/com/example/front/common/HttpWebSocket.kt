package com.example.front.common

import android.util.Log
import com.example.front.data.ChatMessage
import com.example.front.data.Role
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.realm.Realm
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import java.text.ParseException

public class HttpWebSocket : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("CHATTING", "Socket Open");

        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("CHATTING", "Socket onMessage string : " + text);

        //TODO: String -> JSONObject Parser
        val gson = Gson()
        val jsonObject = gson.fromJson(text, JsonObject::class.java)

        //TODO: Realm DB 저장
        val db = Realm.getDefaultInstance()
        db.executeTransactionAsync {
            val msg = ChatMessage(
                roomId = jsonObject.get("roomId").asInt,
                message = jsonObject.get("message").asString,
                senderId = jsonObject.get("senderId").asInt,
                senderName = jsonObject.get("senderName").asString,
                senderRole = jsonObject.get("senderRole").asString
            )
            it.insert(msg)
            Log.d("Realm", "onMessage - executeTransactionAsync")
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("CHATTING", "Socket onMessage");

        super.onMessage(webSocket, bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("CHATTING", "Socket onClosing");

        super.onClosing(webSocket, code, reason)
        webSocket.close(1000, null);
        webSocket.cancel();
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("CHATTING", "Socket Closed");

        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("CHATTING", "Socket Connection failed: ${t.message}")

        super.onFailure(webSocket, t, response)
    }
}