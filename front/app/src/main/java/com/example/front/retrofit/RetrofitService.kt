package com.example.front.retrofit

import android.content.Context
import com.example.front.BuildConfig
import com.example.front.data.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val BASE_URL = BuildConfig.BASE_URL



    private lateinit var okHttpClient : OkHttpClient

    fun init(context: Context){
        val authInterceptor = AuthInterceptor(context)
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val reviewService: ReviewApi by lazy {
        retrofit.create(ReviewApi::class.java)
    }


    fun storeService(context: Context): StoreApi {
        init(context)
        return retrofit.create(StoreApi::class.java)
    }

    fun reservationService(context: Context): ReservationService {
        init(context)
        return retrofit.create(ReservationService::class.java)
    }

}
