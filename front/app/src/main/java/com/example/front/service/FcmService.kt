package com.example.front.service

import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FcmService"
    }

    // 새로운 FCM 토큰이 생성되었을 때의 처리 로직
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        // 토큰을 DB에 저장
    }

    // FCM 서버로부터 메세지를 수신했을 때의 처리 로직
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: $message")


        /* 채팅 알림인 경우 */
        //1. Realm DB에 채팅 내역 저장
        //2. Notification ㄱㄱ

        /* 예약 알림인 경우 */
        //1. Notification ㄱㄱ
    }
}