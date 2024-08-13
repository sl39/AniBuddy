package com.example.front.data.request

data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)

data class CheckEmail(
    val email: String
)

data class CheckNickname(
    val nickname: String
)

data class signupRequest(
    val userName: String,
    val email: String,
    val password: String,
    val password2: String,
    val nickname: String,
    val role: String
)