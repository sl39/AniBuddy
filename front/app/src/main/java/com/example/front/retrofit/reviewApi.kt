package com.example.front.retrofit

import android.content.Context
import com.example.front.data.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {

    @GET("/api/review/all")
    suspend fun getAllReviews(): List<ReviewEntity>

    @POST("/api/review/create")
    suspend fun createReview(@Body reviewCreateDto: ReviewCreateDto): String

    @POST("/api/review/createAll")
    suspend fun createAllReviews(@Body reviewCreateDtoList: List<ReviewCreateDto>): String

    @POST("/api/review/creatTags")
    suspend fun createTags(@Body tags: List<String>): String

    @GET("/api/review/{reviewId}/image")
    suspend fun getReviewImages(@Path("reviewId") reviewId: Int): List<ReviewImageEntity>

    @GET("/api/review/{storeId}")
    suspend fun getReviewsByStoreId(@Path("storeId") storeId: Int): List<ReviewEntity>

    companion object {
        fun create(context: Context): ReviewApi {
            val authInterceptor = AuthInterceptor(context)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ReviewApi::class.java)
        }
    }
}

data class ReviewEntity(
    val review: String,
    val createDate: String,
    val updateDate: String,
    val reviewScore: Float,
    val reviewImageList: List<String>,
    val reviewId: Int,
)

data class ReviewCreateDto(
    val review: String,
    val reviewScore: Float
)

data class ReviewImageEntity(
    val id: Int,
    val imageUrl: String
)

data class ReviewObject(
    val ratings: Float,
    val storeTitle: String,
    val reviewContent: String,
    val reviewTime: String,
    val reviewId: Int, // 필수 매개변수
    val storeId: Int,
    val content: String, // 필수 매개변수
    val reviewImageList: List<String>,

)
