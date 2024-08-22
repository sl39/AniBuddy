package com.example.front.receiver

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.front.R
import com.example.front.activity.NotificationListActivity

class AlarmReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager

    @SuppressLint("UnsafeIntentLaunch")
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "onReceive")

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 2
        val channelId = "ReservationChannel"

//        val intent = Intent(context, NotificationListActivity::class.java).apply {
//            putExtra("storeName", storeName)
//        }
//        val pendingIntent: PendingIntent =
//            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val storeName: String = intent.getStringExtra("storeName") as String
        val title: String = storeName
        val body = "한 시간 뒤에 $storeName 예약이 있어요."

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background) //TODO: 변경
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true) // 클릭 시 자동 제거
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        //.setContentIntent(pendingIntent) // 클릭 시 실행될 인텐트

        // 알림 표시
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}