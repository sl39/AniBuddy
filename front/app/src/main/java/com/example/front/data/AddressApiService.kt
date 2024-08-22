package com.example.front.data

import com.example.front.data.response.NewAddressListResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressApiService {

    @GET("getNewAddressListAreaCdSearchAll")
    fun getAddress(
        @Query("serviceKey") serviceKey: String,
        @Query("srchwrd") srchwrd : String,
        @Query("countPerPage") countPerPage: Int,
        @Query("currentPage") currentPage: Int
    ): Call<NewAddressListResponse>


    companion object{
        private const val BASE_URL = "http://openapi.epost.go.kr/postal/retrieveNewAdressAreaCdSearchAllService/retrieveNewAdressAreaCdSearchAllService/"
        val gson : Gson = GsonBuilder().setLenient().create();

        fun create() : AddressApiService {
            val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TikXmlConverterFactory.create(parser))
                .build()
                .create(AddressApiService::class.java)
        }


    }
}