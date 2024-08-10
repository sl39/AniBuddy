package com.example.front.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import com.example.front.R
import com.example.front.databinding.ActivitySignUpBinding
import com.example.front.fragment_signup_select

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val fragmentSignupSelect = fragment_signup_select()
    private val fragmentManager :FragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.SignupActivity)
        fragmentManager.beginTransaction().replace(R.id.Signup_activity,fragmentSignupSelect).commitAllowingStateLoss()


    }
}
