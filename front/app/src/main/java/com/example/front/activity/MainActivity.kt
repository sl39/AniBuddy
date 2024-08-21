package com.example.front.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.front.ApiService
import com.example.front.Permission
import com.example.front.fragment_home
import com.example.front.R
import com.example.front.RetrofitClient
import com.example.front.activity.SearchActivity
import com.example.front.data.ChatApiService
import com.example.front.data.response.FcmTokenResponse
import com.example.front.databinding.ActivityMainBinding
import com.example.front.fragment_chatroom_list
import com.example.front.fragment_following_list
import com.example.front.fragment_profile
import com.example.front.fragment_reservation_list
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentHome = fragment_home()
    private val fragmentReservationList = fragment_reservation_list()
    private val fragmentFollowingList = fragment_following_list()
    private val fragmentChatList = fragment_chatroom_list()
    private val fragmentProfile = fragment_profile()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onStart() {
        super.onStart()
        Permission(this).requestLocation() // 위치 권한 요청
        askNotificationPermission() // 알림 권한 요청

        // 토큰 등록 사실이 확인 되지 않는 경우에 서버에 토큰 전송
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if(!isFcmTokenRegistered()) {
            Log.d("SharedPreferences","mark FcmToken Registered")
            registerFcmToken() // 푸시 알림에 필요한 토큰을 등록
            markFcmTokenRegistered() // 토큰 등록 사실을 표시
        }
    }

    private val userId : Int = 1
    private var apiService : ApiService? = null

    companion object {
        const val EXTRA_DESTINATION = "extra_destination"
        const val DESTINATION_HOME = "home"
        const val DESTINATION_RESERVATION_LIST = "reservation_list"
        const val DESTINATION_FOLLOWING_LIST = "following_list"
        const val DESTINATION_CHAT_LIST = "chat_list"
        const val DESTINATION_PROFILE = "profile"
        const val EXTRA_SELECTED_TAB = "extra_selected_tab"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        fragmentManager.beginTransaction()
           .replace(R.id.menu_frame_layout, fragmentHome)
            .commitAllowingStateLoss()

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())

        handleIntent(intent)

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)
        Log.d("menu?", menu.toString())

        val onMenuItemClickListener =
            menu?.findItem(R.id.menu_search_icon)?.setOnMenuItemClickListener {
                intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
        onMenuItemClickListener
        return true
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            val destination = it.getStringExtra(EXTRA_DESTINATION)
            val transaction = fragmentManager.beginTransaction()

//            val userId = it.getIntExtra("userId", 4)

            updateBottomNavigation(destination)
            navigateToFragment(destination)

            Log.d("MainActivity", "Handling intent for destination: $destination")

            when (destination) {
                DESTINATION_HOME -> transaction.replace(
                    R.id.menu_frame_layout,
                    fragmentHome
                )

                DESTINATION_RESERVATION_LIST -> transaction.replace(
                    R.id.menu_frame_layout,
                    fragmentReservationList
                )

                DESTINATION_FOLLOWING_LIST -> transaction.replace(
                    R.id.menu_frame_layout,
                    fragmentFollowingList
                )

                DESTINATION_CHAT_LIST -> transaction.replace(
                    R.id.menu_frame_layout,
                    fragmentChatList
                )

                DESTINATION_PROFILE -> transaction.replace (
                    R.id.menu_frame_layout,
                    fragmentProfile
                )

                else -> transaction.replace(R.id.menu_frame_layout, fragmentHome)
            }

            transaction.commitAllowingStateLoss()
        }
    }

    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.home -> transaction.replace(
                    R.id.menu_frame_layout,
                    fragmentHome
                )
                    .commitAllowingStateLoss()

                com.example.front.R.id.reservationList -> transaction.replace(
                    com.example.front.R.id.menu_frame_layout,
                    fragmentReservationList
                ).commitAllowingStateLoss()

                com.example.front.R.id.follwingList -> {
                    val fragmentFollowingList = fragment_following_list().apply {
                        arguments = Bundle().apply {
                            putInt("userId", userId)
                            Log.d("userIdCheckFollowingList", "userId = $userId")
                        }
                    }

                    transaction.replace(
                        com.example.front.R.id.menu_frame_layout,
                        fragmentFollowingList
                    ).commitAllowingStateLoss()
                }

                com.example.front.R.id.chatList -> transaction.replace(
                    com.example.front.R.id.menu_frame_layout,
                    fragmentChatList
                )
                    .commitAllowingStateLoss()

                com.example.front.R.id.profile -> {
                    val fragmentProfile = fragment_profile().apply {
                        arguments = Bundle().apply {
                            putInt("userId", userId)
                            Log.d("userIdCheck3", "userId = $userId")
                        }
                    }

                    transaction.replace(
                        com.example.front.R.id.menu_frame_layout,
                        fragmentProfile
                    )
                        .commitAllowingStateLoss()
                }
            }

            return true
        }

    }

    private fun updateBottomNavigation(destination: String?) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_menu_bottom_navigation)
        when (destination) {
            DESTINATION_HOME -> bottomNavigationView.selectedItemId = R.id.home
            DESTINATION_RESERVATION_LIST -> bottomNavigationView.selectedItemId = R.id.reservationList
            DESTINATION_FOLLOWING_LIST -> bottomNavigationView.selectedItemId = R.id.follwingList
            DESTINATION_CHAT_LIST -> bottomNavigationView.selectedItemId = R.id.chatList
            DESTINATION_PROFILE -> bottomNavigationView.selectedItemId = R.id.profile
           }
        }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val selectedTab = intent.getIntExtra(EXTRA_SELECTED_TAB, R.id.home)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_menu_bottom_navigation)
        bottomNavigationView.selectedItemId = selectedTab

        handleIntent(intent)

    }

    private fun navigateToFragment(destination: String?) {
        val fragment = when (destination) {
            DESTINATION_HOME -> fragmentHome
            DESTINATION_RESERVATION_LIST -> fragment_reservation_list()
            DESTINATION_FOLLOWING_LIST -> fragment_following_list()
            DESTINATION_CHAT_LIST -> fragment_chatroom_list()
            DESTINATION_PROFILE -> fragment_profile()
            else -> fragmentHome
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.menu_frame_layout, fragment)
            .commit()
    }

    // 권한 요청 런처
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Permission", "알림 권한이 허용되었습니다")
        } else {
            Log.d("Permission", "알림 권한이 거부되었습니다")
        }
    }

    // 알림 권한 확인 및 요청
    private fun askNotificationPermission() {
        // TIRAMISU 버전 이하라면 권한 요청 필요X
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        // 알림 권한이 이미 허용된 상태인지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "알림 권한이 이미 허용되어 있습니다")
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialogBuilder
                .setTitle("알림 권한 요청")
                .setMessage("알림을 받기 위해서는 알림 권한 설정이 필요합니다.")
                .setNegativeButton("취소") { dialog, which ->
                    Log.d("Permission", "알림 권한이 거부되었습니다")
                }
                .setPositiveButton("알림 권한 설정") { dialog, which ->
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            val dialog: AlertDialog = alertDialogBuilder.create()
            dialog.show()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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



//                com.example.front.R.id.profile -> transaction.replace(
//                    com.example.front.R.id.menu_frame_layout,
//                    fragmentProfile
//                )
//                    .commitAllowingStateLoss()
//            }

//        private fun updateBottomNavigation(destination: String?) {
//            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_menu_bottom_navigation)
//            when (destination) {
//                DESTINATION_HOME -> bottomNavigationView.selectedItemId = R.id.home
//                DESTINATION_RESERVATION_LIST -> bottomNavigationView.selectedItemId = R.id.reservationList
//                DESTINATION_FOLLOWING_LIST -> bottomNavigationView.selectedItemId = R.id.follwingList
//                DESTINATION_CHAT_LIST -> bottomNavigationView.selectedItemId = R.id.chatList
//                DESTINATION_PROFILE -> bottomNavigationView.selectedItemId = R.id.profile
//            }
//        }