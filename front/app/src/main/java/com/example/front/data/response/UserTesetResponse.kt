package com.example.front.data.response

import java.time.LocalDate

data class UserTesetResponse(
    val userName: String,
    )


data class MainReviewSimpleResponseDto(
    val storeId: Int,
    val distance: Double,
    val createdDate: String,
    val review: String,
    val imageUrl: String,
    val modifiedDate : String,
    val category: String
)