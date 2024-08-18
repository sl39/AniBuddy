package com.example.front.data

import com.example.front.data.response.LocationResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApiService {

    @GET("address.json")
    fun getLocation(
        @Header("Authorization") key : String,
        @Query("query") query: String
    ): Call<LocationResponse>

    companion object{
        private const val BASE_URL = "https://dapi.kakao.com/v2/local/search/"
        val gson : Gson = GsonBuilder().setLenient().create();

        fun create() : LocationApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(LocationApiService::class.java)
        }


    }


}