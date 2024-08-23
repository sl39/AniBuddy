package com.example.front.data.response

data class FcmTokenResponse(
    val clientId: Int,
    val clientRole: String,
    val fcmToken: String
)
