package com.example.front.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.front.R
import com.example.front.data.ApiService
import com.example.front.data.response.UserTesetResponse
import com.example.front.databinding.ActivityLoginBinding
import com.example.front.fragment_owner_login
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val fragmentManager: FragmentManager = supportFragmentManager


    private val fragmentOwnerLogin = fragment_owner_login()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = ApiService.create(this@LoginActivity)
        val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)


        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fragmentManager.beginTransaction().replace(R.id.login_activity,fragmentOwnerLogin).commitAllowingStateLoss()
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
                    fragmentManager.beginTransaction().replace(R.id.login_activity,fragmentOwnerLogin).commitAllowingStateLoss()
                }
            }

            override fun onFailure(call: Call<UserTesetResponse>, t: Throwable) {
                fragmentManager.beginTransaction().replace(R.id.login_activity,fragmentOwnerLogin).commitAllowingStateLoss()
                val show = Toast.makeText(this@LoginActivity, "로그인 하쇼", Toast.LENGTH_SHORT)
                    .show();
            }
        })

    }
}


