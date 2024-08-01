package com.example.front.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import com.example.front.fragment_home
import com.example.front.R
import com.example.front.databinding.ActivityMainBinding
import com.example.front.fragment_chat_list
import com.example.front.fragment_following_list
import com.example.front.fragment_profile
import com.example.front.fragment_reservation_list
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentHome = fragment_home()
    private val fragmentReservationList = fragment_reservation_list()
    private val fragmentFollowingList = fragment_following_list()
    private val fragmentChatList = fragment_chat_list()
    private val fragmentProfile = fragment_profile()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        fragmentManager.beginTransaction().replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.home_menu_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)

        val onMenuItemClickListener =
            menu?.findItem(R.id.menu_search_icon)?.setOnMenuItemClickListener {
                intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
        onMenuItemClickListener
        return true
    }

    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.home -> transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss()
                R.id.reservationList -> transaction.replace(R.id.menu_frame_layout, fragmentReservationList).commitAllowingStateLoss()
                R.id.follwingList -> transaction.replace(R.id.menu_frame_layout, fragmentFollowingList).commitAllowingStateLoss()
                R.id.chatList -> transaction.replace(R.id.menu_frame_layout, fragmentChatList).commitAllowingStateLoss()
                R.id.profile -> transaction.replace(R.id.menu_frame_layout, fragmentProfile).commitAllowingStateLoss()
            }

            return true
        }
    }
}
