package com.example.front.data

import android.content.Context
import com.example.front.BuildConfig
import com.example.front.data.response.ChatRoomResponse
import com.example.front.data.response.FcmTokenResponse
import com.example.front.data.response.NotificationResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {

    //알림 리스트 불러오기
    @GET("/api/notifications")
    fun getNotificationList(): Call<List<NotificationResponse>>

    //채팅방 생성하기 (같은 멤버의 채팅방이 이미 존재한다면 새롭게 생성하지 않음)
    @POST("/api/user/chatrooms")
    fun makeChatRoom(@Body storeId: Int): Call<ChatRoomResponse>

    //채팅방 목록 불러오기
    @GET("/api/chatrooms")
    fun getChatRoomList(): Call<List<ChatRoomResponse>>

    //새로 발급된 Fcm Token 서버에 저장하기
    @POST("/api/fcm")
    fun registerFcmToken(@Body fcmToken: String): Call<FcmTokenResponse>

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        val gson : Gson = GsonBuilder().setLenient().create();

        fun create(context : Context) : ChatApiService {
            val authInterceptor = AuthInterceptor(context)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ChatApiService::class.java)

            return retrofit
        }
    }
}