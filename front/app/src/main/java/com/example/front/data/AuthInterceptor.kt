package com.example.front.data

import android.content.Context
import android.util.Log
import com.example.front.data.request.TokenReqeust
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class AuthInterceptor(private val context: Context) : Interceptor {
    private val userPreferencesRepository by lazy {
        preferencesRepository.getUserPreferencesRepository(context)
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")  // 실제 API 베이스 URL로 교체하세요.
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            userPreferencesRepository.getAccessToken.first()
        }
//        isTokenValid(accessToken)
        var request = chain.request().newBuilder()

        if (!accessToken.equals("")) {
            request.addHeader("Authorization", "Bearer $accessToken")
        }

        var response = chain.proceed(request.build())

        if (response.code != 200) {
            var refreshToken = runBlocking {
                userPreferencesRepository.getRefreshToken.first()
            }

            if (!refreshToken.equals("")) {
                val newAccessToken = runBlocking { refreshAccessToken(refreshToken) }
                if (!newAccessToken.equals("")) {
                    request = chain.request().newBuilder()
                    request.removeHeader("Authorization")
                    request.addHeader("Authorization", "Bearer $newAccessToken")
                    response = chain.proceed(request.build())
                }
            }
        }

        return response
    }

    private suspend fun refreshAccessToken(refreshToken: String): String {
        val data = TokenReqeust("토큰 요청")
        var newAccessToken = ""
        val headers = HashMap<String, String>()
        headers["Authorization-refresh"] = "Bearer $refreshToken"


        try {
            val response = api.autoLogin(data, headers).execute()
            if (response.code() == 200) {
                val body = response.body()
                if (body != null) {
                    newAccessToken = response.headers()["Authorization"].orEmpty()
                    val newRefreshToken = response.headers()["Authorization-refresh"].orEmpty()
                    userPreferencesRepository.setAccessToken(newAccessToken)
                    userPreferencesRepository.setRefreshToken(newRefreshToken)
                    Log.d("엑세스 토큰", newAccessToken)
                    Log.d("리프레쉬 토큰", newRefreshToken)
                }
            } else {
                userPreferencesRepository.setAccessToken("")
                userPreferencesRepository.setRefreshToken("")
            }
        } catch (e: Exception) {
            userPreferencesRepository.setAccessToken("")
            userPreferencesRepository.setRefreshToken("")
        }

        return newAccessToken
    }

//    @OptIn(ExperimentalEncodingApi::class)
//    fun getBase64decode(content: String?): String {
//        return String(Base64.decode(content!!, 0))
//    }
//
//    fun isTokenValid(token: String?): Boolean {
//        if(token == null){
//            return false
//        }
//
//        val valid = token.split(".")
//        if (valid.size != 3) {
//            return false // JWT는 반드시 세 부분으로 구성되어 있어야 합니다.
//        }
//        val gson = Gson()
//        val mapType: Type = object : TypeToken<Map<String, Any>>() {}.type
//        val payload = getBase64decode(valid[1])
//        val payloadMap: Map<String, String> = gson.fromJson(payload, mapType)
//        Log.d("토큰 토큰", payload)
//        Log.d("Toekn 시간 시간", payloadMap.get("exp").toString())
//        return true
//    }
}
