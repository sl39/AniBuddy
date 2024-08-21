package com.example.front.activity

import android.content.Intent
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
import com.example.front.databinding.ActivityOwenerMainBinding
import com.example.front.fragment_chat_list_owner
import com.example.front.fragment_profile_owner
import com.example.front.fragment_reservation_list_owner
import com.google.android.material.bottomnavigation.BottomNavigationView

class OwenerMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOwenerMainBinding
    private val fragmentManager = supportFragmentManager
    private val fragmentReservationListOwner = fragment_reservation_list_owner()
    private val fragmentChatListOwner = fragment_chat_list_owner()
    private val ownerStoreListFragment = OwnerStoreListFragment()

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




}