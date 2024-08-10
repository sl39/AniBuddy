package com.example.front.data.request

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)

data class checkEmail(
    val email: String
)

data class checknickname(
    val nickname: String
)