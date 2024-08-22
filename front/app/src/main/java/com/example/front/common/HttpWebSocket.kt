package com.example.front.common

import android.util.Log
import com.example.front.data.ChatMessage
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.realm.Realm
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

public class HttpWebSocket : WebSocketListener() {

    private val TAG = "CHATTING"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "Socket Open");

        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(TAG, "Socket onMessage string : " + text);

        // String -> JSONObject Parser
        val gson = Gson()
        val jsonObject = gson.fromJson(text, JsonObject::class.java)

        // Realm DB 저장
        val db = Realm.getDefaultInstance()
        db.executeTransaction{
            val msg = ChatMessage(
                roomId = jsonObject.get("roomId").asInt,
                message = jsonObject.get("message").asString,
                senderId = jsonObject.get("senderId").asInt,
                senderRole = jsonObject.get("senderRole").asString,
                senderName = jsonObject.get("senderName").asString,
                senderImageUrl = jsonObject.get("senderImageUrl").asString,
                receiverId = jsonObject.get("receiverId").asInt,
                receiverRole = jsonObject.get("receiverRole").asString,
                receiverName = jsonObject.get("receiverName").asString,
                receiverImageUrl = jsonObject.get("receiverImageUrl").asString
            )
            it.insert(msg)
        }
        Log.d("Realm", jsonObject.get("message").asString)
        db.close()
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d(TAG, "Socket onMessage")

        super.onMessage(webSocket, bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "Socket onClosing")

        super.onClosing(webSocket, code, reason)
//        webSocket.close(1000, null)
//        webSocket.cancel()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "Socket Closed")

        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e(TAG, "Socket Connection failed: ${t.message}")

        super.onFailure(webSocket, t, response)
    }
}