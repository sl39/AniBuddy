package com.example.front.data

import androidx.datastore.preferences.protobuf.Api
import com.example.front.data.request.LoginRequest
import com.example.front.data.request.TokenReqeust
import com.example.front.data.response.LoginResponse
import com.example.front.data.response.TokenResponse
import com.example.front.data.response.UserTesetResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApiService{
    @POST("/api/auth/login")
    @Headers(
        "content-type: application/json"
    )
    fun userLogin(
        @Body jsonParams : LoginRequest,
        ): Call<LoginResponse>

    companion object{
        private const val BASE_URL = "http://10.0.2.2:8080"
        val gson : Gson = GsonBuilder().setLenient().create();

        fun create() : LoginApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(LoginApiService::class.java)
        }


    }
}

