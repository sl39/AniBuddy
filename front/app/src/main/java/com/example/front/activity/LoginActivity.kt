package com.example.front.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.front.R
import com.example.front.data.ApiService
import com.example.front.data.UserPreferencesRepository
import com.example.front.data.preferencesRepository
import com.example.front.data.response.UserTesetResponse
import com.example.front.databinding.ActivityLoginBinding
import com.example.front.fragment_owner_login
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private val fragmentOwnerLogin = fragment_owner_login()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = ApiService.create(this@LoginActivity)
        val userIntent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
        val ownerIntent: Intent = Intent(this@LoginActivity, OwenerMainActivity::class.java)
        userPreferencesRepository = preferencesRepository.getUserPreferencesRepository(this@LoginActivity)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager.beginTransaction().replace(R.id.login_activity,fragmentOwnerLogin).commitAllowingStateLoss()

        api.getUserTest().enqueue(object : Callback<UserTesetResponse>{
            override fun onResponse(
                call: Call<UserTesetResponse>,
                response: Response<UserTesetResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { Log.d("이게 userId 로 받아와야 됨 userName", it.role) }

                    var tag = runBlocking {
                        userPreferencesRepository.getUserType.first()
                    }
                    if(tag.equals("USER")){
                        startActivity(userIntent)
                    } else{
                        startActivity(ownerIntent)
                    }

                } else {
                    onFailure(call, Throwable("Unsuccessful response"))
                    fragmentManager.beginTransaction().replace(R.id.login_activity,fragmentOwnerLogin).commitAllowingStateLoss()
                    val show = Toast.makeText(this@LoginActivity, "로그인 하세요", Toast.LENGTH_SHORT)
                        .show();
                }
            }

            override fun onFailure(call: Call<UserTesetResponse>, t: Throwable) {
                fragmentManager.beginTransaction().replace(R.id.login_activity,fragmentOwnerLogin).commitAllowingStateLoss()
                val show = Toast.makeText(this@LoginActivity, "로그인 하세요", Toast.LENGTH_SHORT)
                    .show();
            }
        })

        fragmentManager.beginTransaction().replace(R.id.login_activity,fragmentOwnerLogin).commitAllowingStateLoss()

    }
}


