package com.example.front

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import com.example.front.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentHome = FragmentHome()
    private val fragmentReservationList = fragment_reservation_list()
    private val fragmentFollowingList = fragment_following_list()
    private val fragmentChatList = fragment_chat_list()
    private val fragmentProfile = fragment_profile()




    private val userId : Int = 3
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(com.example.front.R.menu.search, menu)

        val menuItem = menu?.findItem(com.example.front.R.id.menu_search_icon)
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.d("Query", query)
                }
                return true
            }
        })
        return true
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
            DESTINATION_HOME -> FragmentHome()
            DESTINATION_RESERVATION_LIST -> fragment_reservation_list()
            DESTINATION_FOLLOWING_LIST -> fragment_following_list()
            DESTINATION_CHAT_LIST -> fragment_chat_list()
            DESTINATION_PROFILE -> fragment_profile()
            else -> FragmentHome()
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