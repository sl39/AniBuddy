package com.example.front.data

import android.content.Context
import android.util.Log
import com.example.front.activity.dataStore
import com.example.front.data.request.TokenReqeust
import com.example.front.data.response.TokenResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import retrofit2.Callback
import retrofit2.Call
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    private val userPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }
    val api = LoginApiService.create()

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = userPreferencesRepository.getAccessToken.toString()
        val request = chain.request().newBuilder()

        if (accessToken.isNotEmpty()) {
            request.addHeader("Authorization", "Bearer $accessToken")
        }

        val response = chain.proceed(request.build())

        if (response.code == 403) {
            val refreshToken = userPreferencesRepository.getRefreshToken.toString()
            if (refreshToken.isNotEmpty()) {
                val newAccessToken = refreshAccessToken(refreshToken)
                request.addHeader("Authorization", "Bearer $newAccessToken")
                return chain.proceed(request.build())
            } else {
                // Handle the case where the refresh token is not available
                return response
            }
        }

        return response
    }

    private fun refreshAccessToken(refreshToken: String): String {
        // Make an API call to get a new access token using the refresh token
        // and return the new access token
        // ...
        val message = "토큰 요청"
        val data = TokenReqeust(message)
        var newAccessToken = ""

        val headers = HashMap<String, String>()
        headers["Authorization-refresh"] = refreshToken
        api.autoLogin(data,headers).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: retrofit2.Response<TokenResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        newAccessToken = response.headers().get("Authorization").toString()
                        val refreshToken = response.headers().get("Authorization-refresh").toString()
                        GlobalScope.launch {
                            userPreferencesRepository.setAccessToken(newAccessToken)
                            userPreferencesRepository.setRefreshToken(refreshToken)
                            userPreferencesRepository.getAccessToken
                                .onEach { accessToken ->
                                    // accessToken 값 사용
                                    Log.d("AccessToken", accessToken)
                                }
                                .launchIn(this)
                            userPreferencesRepository.getRefreshToken
                                .onEach { refreshToken ->
                                    // refreshToken 값 사용
                                    Log.d("RefreshToken", refreshToken)
                                }
                                .launchIn(this)
                        }
                    }
                    else -> {
                        throw Exception("Failed to refresh access token")
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.d("로그인 통신 실패!", t.message.toString())
            }
        })
        return newAccessToken
    }

}
