package com.example.front.data

import com.example.front.BuildConfig
import com.example.front.data.request.CheckEmail
import com.example.front.data.request.CheckNickname
import com.example.front.data.request.LoginRequest
import com.example.front.data.request.signupRequest
import com.example.front.data.response.LoginResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApiService{
    @POST("/api/auth/login")
    @Headers(
        "content-type: application/json",
        "Authroization: Bearer 1"
    )
    fun ownerLogin(
        @Body jsonParams : LoginRequest,
        ): Call<LoginResponse>

    @POST("/api/auth/signup/check/email")
    @Headers(
        "content-type: application/json"
        ,"Authroization: Bearer 1"
    )
    fun checkEmail(
        @Body jsonParams: CheckEmail,
        ): Call<Boolean>

    @POST("/api/auth/signup/check/nickname")
    @Headers(
        "content-type: application/json",
        "Authroization: Bearer 1"
    )
    fun checkNickname(
        @Body jsonParams : CheckNickname,
    ): Call<Boolean>

    @POST("/api/auth/signup")
    @Headers(
        "content-type: application/json",
        "Authorization: Bearer 1"
    )
    fun signup(
        @Body jsonParams: signupRequest
    ): Call<CheckEmail>


    companion object{
        private const val BASE_URL = BuildConfig.BASE_URL
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

