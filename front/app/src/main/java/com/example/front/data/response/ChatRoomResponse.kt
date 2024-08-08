package com.example.front.data.response

import java.time.LocalDateTime

data class ChatRoomResponse(
    val roomId: Int,
    val otherName: String,
    val otherProfileImageUrl: String
)
