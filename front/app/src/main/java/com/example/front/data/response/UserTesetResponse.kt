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

data class SerachLocationCategoryResponseDto(
    val id: Int,
    val storeName: String,
    val category: String,
    val distance: Double,
    val reviewCount: Int,
    val storeImage: List<String>
)