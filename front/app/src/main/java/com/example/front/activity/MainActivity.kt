package com.example.front.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.front.ApiService
import com.example.front.FragmentReservationList
import com.example.front.Permission
import com.example.front.R
import com.example.front.RetrofitClient
import com.example.front.databinding.ActivityMainBinding
import com.example.front.fragment_chatroom_list
import com.example.front.fragment_following_list
import com.example.front.fragment_home
import com.example.front.fragment_profile
import com.example.front.fragment_reservation_list
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.api.Authentication


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentHome = fragment_home()
    private val fragmentReservationList = fragment_reservation_list()
    private val fragmentFollowingList = fragment_following_list()
    private val fragmentChatList = fragment_chatroom_list()
    private val fragmentProfile = fragment_profile()

    private val userId : Int = 1
    private var apiService : ApiService? = null

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
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

        requestLocationPermission()
//        fragmentManager.beginTransaction()
//           .replace(R.id.menu_frame_layout, fragmentHome)
//            .commitAllowingStateLoss()

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())
//
//        handleIntent(intent)
//
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService::class.java)
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없을 경우, 사용자에게 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            // 권한이 이미 있을 경우, 위치 정보를 사용할 수 있음
            Log.d("123","123")
            handleIntent(intent)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 권한이 부여되면 위치 정보를 사용할 수 있음
                    handleIntent(intent)

                    Log.d("test","test")
                } else {
                    // 권한이 거부되면, 기능 사용 불가
                    Log.d("test","test123")
                    handleIntent(intent)

                }
                return
            }
        }
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

//            val userId = it.getIntExtra("userId", -1)

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
//                            putInt("userId", userId)
                            
//                            Log.d("userIdCheckFollowingList", "userId = $userId")
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
//                            putInt("userId", userId)
//                            Log.d("userIdCheck3", "userId = $userId")
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