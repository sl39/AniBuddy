package com.example.front.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.front.data.LoginApiService
import com.example.front.data.UserPreferencesRepository
import com.example.front.data.request.LoginRequest
import com.example.front.data.response.LoginResponse
import com.example.front.databinding.ActivityLoginBinding
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn_login : Button = binding.loginBtn
        btn_login.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                // 로그인 함수 부분
                val email = binding.editTextUsername.text.toString().trim()
                val password = binding.editTextPassword.text.toString().trim()
                val data = LoginRequest(email,password)

                val api = LoginApiService.create()
                api.userLogin(data).enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        Log.d("로그인 통신 성공 accessToken", response.headers().get("Authorization").toString())
                        Log.d("로그인 통신 성공 refreshToken", response.headers().get("Authorization-refresh").toString())


                        when(response.code()){
                            200 -> {
                                val intent: Intent = Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(intent)
                            }
                            else ->{
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

class loginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(){
    private var viewModelJob : CompletableJob = Job()

    private val uiScope : CoroutineScope =
        CoroutineScope(Dispatchers.Main + viewModelJob)

    var acessToken : LiveData<String> = userPreferencesRepository.getAccessToken.asLiveData()
    var refreshToken : LiveData<String> = userPreferencesRepository.getRefreshToken.asLiveData()

    fun setTokens(){
        uiScope.launch {
            val accessToken = "accessToken"
            val refreshToken = "refreshToken"

            userPreferencesRepository.setAccessandRefreshToken(accessToken,refreshToken)
        }
    }
}