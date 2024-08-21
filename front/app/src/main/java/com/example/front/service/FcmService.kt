package com.example.front.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.front.MessageListActivity
import com.example.front.R
import com.example.front.data.ChatApiService
import com.example.front.data.ChatMessage
import com.example.front.data.response.FcmTokenResponse
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FcmService : FirebaseMessagingService() {

    private val TAG = "FcmToken"

    // 새로운 FCM 토큰이 생성되었을 때의 처리 로직
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "onNewToken: $token")

        // FCM 서버가 토큰을 재발급 했을 경우를 대비
        val chatApi = ChatApiService.create(applicationContext)
        chatApi.registerFcmToken(token).enqueue(object: Callback<FcmTokenResponse>{
            override fun onResponse(
                call: Call<FcmTokenResponse>,
                response: Response<FcmTokenResponse>
            ) {
                if(response.isSuccessful) {
                    response.body()!!.let {
                        Log.d(TAG, "id:${it.clientId} role:${it.clientRole} fcmToken:${it.fcmToken}")
                    }
                }
            }
            override fun onFailure(call: Call<FcmTokenResponse>, t: Throwable) {
                Log.e(TAG, "onFailure - chatApi.registerFcmToken")
            }
        })
    }

    // FCM 서버로부터 메세지를 수신했을 때의 처리 로직
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: ${message.data["type"]}")

        val data: Map<String, String> = message.data
        val type = data["type"]
        val jsonMsg = data["data"]

        if(type.equals("chat")) {
            /* 채팅 알림인 경우 */
            val gson = Gson()
            val jsonObject = gson.fromJson(jsonMsg, JsonObject::class.java)

            //1. Realm DB에 채팅 내역 저장
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

            //2. Notification 생성
            val title = jsonObject.get("senderName").asString
            val body = jsonObject.get("message").asString

            showNotification(title, body, jsonObject)
        }
        else if(type.equals("reservation")) {
            /* 예약 알림인 경우 */
            //1. AlarmManager - 일회성 알람 예약

            //2. BroadCastReceiver - onReceive 에서 Notification 발생
        }
    }

    private fun showNotification(title: String, body: String, jsonObject: JsonObject) {
        val notificationId = 1
        val channelId = "ChatChannel"

        val intent = Intent(this, MessageListActivity::class.java).apply {
            putExtra("roomId", jsonObject.get("roomId").asInt)

            putExtra("myId", jsonObject.get("receiverId").asInt)
            putExtra("myRole", jsonObject.get("receiverRole").asString)
            putExtra("myName", jsonObject.get("receiverName").asString)
            putExtra("myImageUrl", jsonObject.get("receiverImageUrl").asString)

            putExtra("otherId", jsonObject.get("senderId").asInt)
            putExtra("otherRole", jsonObject.get("senderRole").asString)
            putExtra("otherName", jsonObject.get("senderName").asString)
            putExtra("otherImageUrl", jsonObject.get("senderImageUrl").asString)

            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background) //TODO: 변경
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true) // 클릭 시 자동 제거
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // 클릭 시 실행될 인텐트
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        // 알림 표시
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}