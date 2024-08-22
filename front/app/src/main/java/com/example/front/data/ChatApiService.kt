package com.example.front.data

import android.content.Context
import com.example.front.data.response.ChatRoomResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatApiService {

    //채팅방 목록 불러오기
    @GET("/api/chatrooms")
    fun getChatRoomList(@Query("myId") id:Int, @Query("role") role:String): Call<List<ChatRoomResponse>>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080" //localhost
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