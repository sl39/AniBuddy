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
import com.example.front.data.LoginApiService
import com.example.front.data.UserPreferencesRepository
import com.example.front.data.request.LoginRequest
import com.example.front.data.response.LoginResponse
import com.example.front.databinding.ActivityLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val userPreferencesRepository by lazy {
        UserPreferencesRepository(this@LoginActivity.dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = LoginApiService.create()

        val intent: Intent = Intent(this@LoginActivity,MainActivity::class.java)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        GlobalScope.launch {
//            userPreferencesRepository.getAccessToken
//                .onEach { accessToken ->
//                    // accessToken 값 사용
//                    if(accessToken.toString().equals("")){
//                        startActivity(intent)
//                    }
//                    else{
//                        setContentView(binding.root)
//
//                    }
//                }
//                .launchIn(this)
//            userPreferencesRepository.getRefreshToken
//                .onEach { accessToken ->
//                    // accessToken 값 사용
//                    Log.d("RefreshToken", accessToken)
//                }
//                .launchIn(this)
//        }



        val btn_login : Button = binding.loginBtn
        btn_login.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                // 로그인 함수 부분
                val email = binding.editTextUsername.text.toString().trim()
                val password = binding.editTextPassword.text.toString().trim()
                val data = LoginRequest(email,password)

                api.userLogin(data).enqueue(object : Callback<LoginResponse>{
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
                                    userPreferencesRepository.getAccessToken
                                        .onEach { accessToken ->
                                            // accessToken 값 사용
                                            Log.d("AccessToken", accessToken)
                                        }
                                        .launchIn(this)
                                    userPreferencesRepository.getRefreshToken
                                        .onEach { accessToken ->
                                            // accessToken 값 사용
                                            Log.d("RefreshToken", accessToken)
                                        }
                                        .launchIn(this)
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

