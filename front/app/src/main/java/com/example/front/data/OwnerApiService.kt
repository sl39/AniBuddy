package com.example.front.data

import android.content.Context
import com.example.front.data.response.OwnerCreateStore
import com.example.front.data.response.OwnerStoreListResponse
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

interface OwnerApiService {

    @GET("/api/owner/stores")
    fun getOwnerStoreList(): Call<List<OwnerStoreListResponse>>

    @POST("/api/store/create")
    fun createStore(
        @Body jsonParams : OwnerCreateStore
    )



    companion object{
        private const val BASE_URL = "http://10.0.2.2:8080"
        val gson : Gson = GsonBuilder().setLenient().create();

        fun create(context: Context) : OwnerApiService {
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
                .create(OwnerApiService::class.java)
        }


    }
}