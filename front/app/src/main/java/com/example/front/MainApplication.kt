package com.example.front

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import io.realm.Realm
import io.realm.RealmConfiguration

class MainApplication : Application() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("fcm_token_registered", false).apply()

        super.onCreate()

        // Realm DB 초기화
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("chat.db")
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)

        // Notification 채널 생성
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val chatChannel = NotificationChannel("ChatChannel", "채팅 알림", NotificationManager.IMPORTANCE_HIGH)
                .apply { description = "notify chat info" }
            val reservationChannel = NotificationChannel("ReservationChannel", "예약 알림", NotificationManager.IMPORTANCE_HIGH)
                .apply { description = "notify reservation info" }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(chatChannel)
            notificationManager.createNotificationChannel(reservationChannel)
        }
    }
}