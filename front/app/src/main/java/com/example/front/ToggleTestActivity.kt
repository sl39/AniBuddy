package com.example.front

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToggleTestActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var userId: Int = 0
    private var storeId: Int = 0
    private lateinit var sotreRoaddress : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toggletest)

        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService::class.java)

        userId = intent.getIntExtra("userId", -1)
        storeId = intent.getIntExtra("storeId", -1)

        Log.d("Test userId", "userId=${userId}")
        Log.d("Test storeId", "storeId=${storeId}")

        val toggleButton: Button = findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener {
            if (userId != -1 && storeId != -1) {
                toggleFollowing()
            } else {
                showToast("잘못된 사용자 또는 프로필 정보")
            }
        }
    }

    private fun toggleFollowing() {
        apiService.toggleFollowing(userId, storeId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("팔로우 상태가 변경되었습니다.")
                } else {
                    showToast("팔로우 상태 변경 실패")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("네트워크 오류 발생")
                Log.e("ToggleTestActivity", "Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}




//    private fun toggleFollowing() {
//        apiService.toggleFollowing(storeId).enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful) {
//                    val profileCategory = response.body() ?: ""
//                    toggleFollowing(profileCategory)
//                } else {
//                    showToast("프로필 카테고리 요청 실패")
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                showToast("네트워크 오류 발생")
//                Log.e("ToggleTestActivity", "Error: ${t.message}")
//            }
//        })
//    }
//
//