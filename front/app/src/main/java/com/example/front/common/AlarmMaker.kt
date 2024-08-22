package com.example.front.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.front.activity.NotificationListActivity
import com.example.front.data.AlarmId
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Random

class AlarmMaker {

    private lateinit var alarmManager: AlarmManager

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAlarm(context: Context, reservationId:Int, reservationDate: LocalDateTime, storeName:String): Int {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 시간 정보 추출
        val year = reservationDate.year
        val month = reservationDate.monthValue-1
        val day = reservationDate.dayOfMonth
        val hour = reservationDate.hour

        // 당일 예약 시간 1시간 전 알림
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month) //1월=0
            set(Calendar.DAY_OF_MONTH, day) //1일=1
            set(Calendar.HOUR_OF_DAY, hour-1)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(context, NotificationListActivity::class.java).apply {
            putExtra("storeName", storeName)
        }
        val pendingIntentId: Int = Random().nextInt(1000)

        // pendingIntentId(requestCode)는 PendingIntent의 고유 식별자!!
        val pendingIntent = PendingIntent.getBroadcast(
            context, pendingIntentId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Realm - pendingIntentId 저장
        val config = RealmConfiguration.Builder()
            .name("alarm.db")
            .schemaVersion(1)
            .build()
        val db = Realm.getInstance(config)
        db.executeTransaction{
            val alarmId = AlarmId(reservationId, pendingIntentId)
            it.insert(alarmId)
        }
        db.close()
        Log.d("Realm", "Alarm Id 저장: $pendingIntentId")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, //실제 시간에 기반한 알람
            calendar.timeInMillis, //알람 발생 시간
            pendingIntent //알람이 울릴때 실행될 작업 - Broadcast 지정
        )

        return pendingIntentId
    }

    fun removeAlarm(context: Context, reservationId: Int) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Realm에서 reservationId로 pendingIntentId 조회
        val config = RealmConfiguration.Builder()
            .name("alarm.db")
            .schemaVersion(1)
            .build()
        val db = Realm.getInstance(config)

        val alarmId: RealmResults<AlarmId> = db.where<AlarmId>()
            .equalTo("reservationId", reservationId)
            .sort("createdAt")
            .findAll()
        val pendingIntentId: Int = alarmId.last()!!.alarmId

        db.close()
        Log.d("Realm", "Alarm Id 조회: $pendingIntentId")

        val intent = Intent(context, NotificationListActivity::class.java)

        // 예약건에 저장되어 있는 pendingIntentId로 동일한 PendingIntent 얻어오기
        val pendingIntent = PendingIntent.getBroadcast(
            context, pendingIntentId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }
}