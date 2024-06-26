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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.front.R
import com.example.front.data.UserPreferencesRepository
import com.example.front.databinding.ActivityLoginBinding
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

class loginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(){
    private var viewModelJob : CompletableJob = Job()

    private val uiScope : CoroutineScope =
        CoroutineScope(Dispatchers.Main + viewModelJob)

    var acessToken : LiveData<String> = userPreferencesRepository.getAccessToken.asLiveData()
    var refreshToken : LiveData<String> = userPreferencesRepository.getRefreshToken.asLiveData()

    fun setTokens(){
        uiScope.launch {
            val accessToken = "accessToken"
            val refreshToken = "refreshToken"

            userPreferencesRepository.setAccessandRefreshToken(accessToken,refreshToken)
        }
    }
}