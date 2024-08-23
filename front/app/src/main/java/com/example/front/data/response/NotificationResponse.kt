package com.example.front.data.response

data class NotificationResponse(
    val notification_type: String,
    val notified_at: String,
    val title: String,
    val content: String,
    val isRead: Boolean,
)
