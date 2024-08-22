package com.example.front.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.LocalTime

interface ReservationService {
    @POST("/api/reservation")
    suspend fun createReservation(@Body reservationRequest: ReservationRequest): Response<ReservationResponse>

    @GET("/api/reservation")
    suspend fun getReservations(@Query("reservationId") resvationId: Int): Response<ReservationDetail>
}

data class ReservationRequest(
    val reservationTime : String ,
    val info: String,
    val storeId: Int
)

data class ReservationResponse(
    val resvationId: Int,
)

data class Reservation(
    val id: Int, // 데이터베이스에 맞춰 Long 타입으로 수정
    val info: String, // 세부사항 (nullable로 변경)
    val storeId: String, // 매장 ID
    val reservationTime : LocalDateTime,
    val storeName: String, // 매장 이름
    val storeAddress: String // 매장 주소
)

data class ReservationDetail(
    val reservationDateTime : String,
    val storeName : String,
    val storeLocation : String,
    val info: String,
)
