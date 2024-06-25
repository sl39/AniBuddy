package com.example.front.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.front.R
import com.example.front.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn_login : Button = binding.loginBtn
        btn_login.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent: Intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
            }
        })

    }

}