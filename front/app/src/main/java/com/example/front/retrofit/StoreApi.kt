package com.example.front.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface StoreApi {
    @GET("api/store/all")
    fun getAllStores(): Call<List<Store>>

    @GET("api/store/{storeId}")
    suspend fun getStoreInfo(@Path("storeId") id: Int): Response<Store>

    @POST("api/store/create")
    fun createStore(@Body storeCreateDto: StoreCreateDto): Call<ApiResponse>

    @GET("api/store/main")
    fun getMainStore(
        @Query("mapx") mapx: Double,
        @Query("mapy") mapy: Double,
        @Query("category") category: String
    ): Call<List<MainReviewSimpleResponseDto>>

    // 팔로우 메서드
    @POST("api/store/follow")
    suspend fun followStore(@Body followRecord: FollowRecord): Response<Void>
    @DELETE("api/store/follow")
    suspend fun unfollowStore(@Body followRecord: FollowRecord): Response<Void>
}

data class Store(
    val id: Int,
    val storeName: String,
    val address: String,
    val roadaddress: String,
    val storeInfo: String,
    val phoneNumber: String,
    val openday: String,
    val mapx: Double,
    val mapy: Double,
    val district: String,
    val storeImageList: List<String>,
)

data class StoreCreateDto(
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    val openHours: String,
    val menu: String,
    val facilities: String,
    val parking: String
)

data class ApiResponse(  // 이름 변경
    val message: String
)

data class MainReviewSimpleResponseDto(
    val id: Int,
    val title: String
)
data class FollowRecord(
    val userId: Int, // 사용자 ID
    val storeId: Int // 매장 ID
)