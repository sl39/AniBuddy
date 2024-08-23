package com.example.front.data.response

data class ChatRoomResponse(
    val roomId: Int,
    val myName: String,
    val myId: Int,
    val myRole: String,
    val myImageUrl: String,
    val otherName: String,
    val otherImageUrl: String,
    val otherId: Int,
    val otherRole: String
)
