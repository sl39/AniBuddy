package com.example.front.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.front.data.ApiService
import com.example.front.data.LoginApiService
import com.example.front.data.UserPreferencesRepository
import com.example.front.data.request.LoginRequest
import com.example.front.data.response.LoginResponse
import com.example.front.data.response.UserTesetResponse
import com.example.front.databinding.ActivityLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val userPreferencesRepository by lazy {
        UserPreferencesRepository(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginApi = LoginApiService.create()
        val api = ApiService.create(this@LoginActivity)
        val intent: Intent = Intent(this@LoginActivity,MainActivity::class.java)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        api.getUserTest().enqueue(object : Callback<UserTesetResponse>{
            override fun onResponse(
                call: Call<UserTesetResponse>,
                response: Response<UserTesetResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { Log.d("이게 userId 로 받아와야 됨 userName", it.userName) }
                    startActivity(intent)
                } else {
                    onFailure(call, Throwable("Unsuccessful response"))
                }
            }

            override fun onFailure(call: Call<UserTesetResponse>, t: Throwable) {
                val show = Toast.makeText(this@LoginActivity, "로그인 하쇼", Toast.LENGTH_SHORT)
                    .show();
            }
        })




        val btn_login : Button = binding.loginBtn
        btn_login.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                // 로그인 함수 부분
                val email = binding.editTextUsername.text.toString().trim()
                val password = binding.editTextPassword.text.toString().trim()
                val data = LoginRequest(email,password)

                loginApi.userLogin(data).enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        when(response.code()){
                            200 -> {
                                val accessToken = response.headers().get("Authorization").toString()
                                val refreshToken =  response.headers().get("Authorization-refresh").toString()
                                GlobalScope.launch {
                                    userPreferencesRepository.setAccessToken(accessToken)
                                    userPreferencesRepository.setRefreshToken(refreshToken)
                                }

                                startActivity(intent)
                            }
                            else ->{
                                val show = Toast.makeText(this@LoginActivity, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT)
                                    .show();
                                Log.d("로그인 실패", "아이디와 비밀번호를 확인하세요")
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("로그인 통신 실패!", t.message.toString())

                    }
                })

            }
        })
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_preferences")

