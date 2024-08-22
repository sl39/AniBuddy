package com.example.front.activity

import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.front.AddressSearchFragment
import com.example.front.OwnerStoreListFragment
import com.example.front.R
import com.example.front.StoreAddFragment
import com.example.front.databinding.ActivityOwnerBinding

class OwnerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOwnerBinding
    private val fragmentManager : FragmentManager = supportFragmentManager
    private val ownerStoreListFragment = OwnerStoreListFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager.beginTransaction().replace(R.id.ownerActivity, ownerStoreListFragment).commitAllowingStateLoss()


    }

}