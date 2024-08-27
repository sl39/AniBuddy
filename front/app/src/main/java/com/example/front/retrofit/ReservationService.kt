package com.example.front.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.LocalTime

interface ReservationService {
    @POST("/api/reservation")
    suspend fun createReservation(@Body reservationRequest: ReservationRequest): Response<ReservationResponse>

    @PUT("/api/reservation")
    suspend fun updateReservation(@Body reservationRequest: ReservationUpdateRequest): Response<ReservationResponse>


    @GET("/api/reservation")
    suspend fun getReservations(@Query("reservationId") resvationId: Int): Response<ReservationDetail>

    @DELETE("/api/reservation")
    suspend fun deleteReservation(@Query("reservationId") resvationId: Int) : Response<DeleteMessage>

    @GET("/api/reservation/all")
    suspend fun getAllReservations() : Response<List<Reservation>>

    @PUT("/api/reservation/state")
    suspend fun updateReservationState(@Body res : UpdateReservationStateRequest) : Response<ReservationResponse>

    // 여기서 부터는 owner
    @GET("/api/reservation/owner/all")
    suspend fun getAllReservationsOwner() : Response<List<Reservation>>

    @GET("/api/reservation/owner")
    suspend fun getOwnerReservations(@Query("reservationId") resvationId: Int): Response<ReservationDetail>

}

data class UpdateReservationStateRequest(
    val reservationId: Int,
    val state: Int
)

data class DeleteMessage(
    val message : String
)

data class ReservationRequest(
    val reservationTime : String ,
    val info: String,
    val storeId: Int
)

data class ReservationUpdateRequest(
    val reservationId: Int,
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
    val storeId: Int, // 매장 ID
    val reservationTime : String,
    val storeName: String, // 매장 이름
    val storeAddress: String, // 매장 주소
    val state : Int
)

data class ReservationDetail(
    val reservationDateTime : String,
    val storeName : String,
    val storeLocation : String,
    val info: String,
    val reservationId: Int,
    val storePhoneNumber: String,
    val storeId: Int,
    val state : Int
)
