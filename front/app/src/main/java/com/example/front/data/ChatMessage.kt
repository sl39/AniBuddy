package com.example.front.data

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

open class ChatMessage() : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    @RequiresApi(Build.VERSION_CODES.O)
    var createdAt: String = LocalDateTime.now().toString()

    var roomId: Int = 0
    var message: String = ""

    var senderId: Int = 0
    var senderRole: String = ""
    var senderName: String = ""
    var senderImageUrl: String = ""

    var receiverId: Int = 0
    var receiverRole: String = ""
    var receiverName: String = ""
    var receiverImageUrl: String = ""

    constructor(
        roomId: Int,
        message: String,
        senderId: Int,
        senderRole: String,
        senderName: String,
        senderImageUrl: String,
        receiverId: Int,
        receiverRole: String,
        receiverName: String,
        receiverImageUrl: String
    ) : this() {
        this.roomId = roomId
        this.message = message
        this.senderId = senderId
        this.senderRole = senderRole
        this.senderName = senderName
        this.senderImageUrl = senderImageUrl
        this.receiverId = receiverId
        this.receiverRole = receiverRole
        this.receiverName = receiverName
        this.receiverImageUrl = receiverImageUrl
    }
}