package com.example.front.data

import android.content.Context
import com.example.front.data.request.TokenReqeust
import com.example.front.data.response.TokenResponse
import com.example.front.data.response.UserTesetResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService{
    @POST("/api/auth/autoLogin")
    @Headers(
        "content-type: application/json"
    )
    fun autoLogin(
        @Body jsonParams: TokenReqeust,
        headers: HashMap<String, String>,
    ): Call<TokenResponse>


    @GET("/api/user")
    fun getUserTest(): Call<UserTesetResponse>


    companion object{
        private const val BASE_URL = "http://10.0.2.2:8080"
        val gson : Gson = GsonBuilder().setLenient().create();

        fun create(context: Context) : ApiService {
            val authInterceptor = AuthInterceptor(context)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }


    }
}
