package com.example.front.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.front.OwnerStoreListFragment
import com.example.front.R
import com.example.front.data.ChatApiService
import com.example.front.data.response.FcmTokenResponse
import com.example.front.databinding.ActivityOwenerMainBinding
import com.example.front.fragment_chat_list_owner
import com.example.front.fragment_profile_owner
import com.example.front.fragment_reservation_list_owner
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OwenerMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOwenerMainBinding
    private val fragmentManager = supportFragmentManager
    private val fragmentReservationListOwner = fragment_reservation_list_owner()
    private val fragmentChatListOwner = fragment_chat_list_owner()
    private val ownerStoreListFragment = OwnerStoreListFragment()
    private lateinit var sharedPreferences: SharedPreferences

    var storeId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwenerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.ownerTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        fragmentManager.beginTransaction()
            .replace(R.id.owner_main, fragmentReservationListOwner)
            .commitAllowingStateLoss()

        val bottomNavigationView: BottomNavigationView =
            findViewById(binding.ownerMenuBottomNavigation.id)
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())

        // 토큰 등록 사실이 확인 되지 않는 경우에 서버에 토큰 전송
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if(!isFcmTokenRegistered()) {
            Log.d("SharedPreferences","mark FcmToken Registered")
            registerFcmToken() // 푸시 알림에 필요한 토큰을 등록
            markFcmTokenRegistered() // 토큰 등록 사실을 표시
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_owner, menu)
        return true
    }

    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.ownerreservationList -> transaction.replace(R.id.owner_main, fragmentReservationListOwner).commitAllowingStateLoss()
                R.id.ownerchatList -> transaction.replace(R.id.owner_main, fragmentChatListOwner).commitAllowingStateLoss()
                R.id.ownerprofile -> transaction.replace(R.id.owner_main, ownerStoreListFragment).commitAllowingStateLoss()
            }

            return true
        }
    }

    private fun registerFcmToken() {

        var token: String? = null

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("FcmToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            token = task.result
            Log.d("FcmToken", "FCM Token is ${token}")

            // 서버에 token 값 저장하기
            val chatApi = ChatApiService.create(this)

            chatApi.registerFcmToken(token!!).enqueue(object: Callback<FcmTokenResponse> {
                override fun onResponse(
                    call: Call<FcmTokenResponse>,
                    response: Response<FcmTokenResponse>
                ) {
                    if(response.isSuccessful) {
                        response.body()!!.let {
                            Log.d("FcmToken", "id:${it.clientId} role:${it.clientRole} fcmToken:${it.fcmToken}")
                        }
                    }
                }
                override fun onFailure(call: Call<FcmTokenResponse>, t: Throwable) {
                    Log.e("FcmToken", "onFailure - $t")
                }
            })
        })
    }

    private fun isFcmTokenRegistered(): Boolean {
        return sharedPreferences.getBoolean("fcm_token_registered", false)
    }

    private fun markFcmTokenRegistered() {
        sharedPreferences.edit().putBoolean("fcm_token_registered", true).apply()
    }



}