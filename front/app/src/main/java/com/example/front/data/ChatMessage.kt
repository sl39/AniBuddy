package com.example.front.data

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

open class ChatMessage(
    var roomId: Int = 0,
    var message: String = "",
    var senderRole: String = Role.USER.toString(),
    var senderId: Int = 0,
    var senderName: String = ""
) : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    @RequiresApi(Build.VERSION_CODES.O)
    var createdAt: String = LocalDateTime.now().toString()
}