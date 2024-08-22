package com.example.front.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.front.R

class AlarmReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 2
        val channelId = "ReservationChannel"

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val storeName: String = intent.getStringExtra("storeName") as String
        val title: String = storeName
        val body: String = "한 시간 뒤에 $storeName 예약이 있어요."

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background) //TODO: 변경
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true) // 클릭 시 자동 제거
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // 클릭 시 실행될 인텐트
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        // 알림 표시
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}