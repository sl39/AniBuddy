package com.example.front

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.front.activity.MainActivity
import com.example.front.data.LoginApiService
import com.example.front.data.UserPreferencesRepository
import com.example.front.data.preferencesRepository
import com.example.front.data.request.LoginRequest
import com.example.front.data.response.LoginResponse
import com.example.front.databinding.FragmentOwnerLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_preferences")
/**
 * A simple [Fragment] subclass.
 * Use the [fragment_owner_login.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_owner_login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOwnerLoginBinding.inflate(inflater,container,false)

        val context = requireContext()
        userPreferencesRepository = preferencesRepository.getUserPreferencesRepository(requireContext())

        val loginApi = LoginApiService.create()
        val intent: Intent = Intent(context, MainActivity::class.java)



        val btn_login : Button = binding.loginBtn
        btn_login.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                // 로그인 함수 부분
                val email = binding.editTextUsername.text.toString().trim()
                val password = binding.editTextPassword.text.toString().trim()
                val data = LoginRequest(email,password,"USER")
                binding.userLoginBth.setOnClickListener {
                    binding.userLoginBth.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
                    binding.ownerLoginBtn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    binding.userLoginBth.isEnabled = false
                    binding.ownerLoginBtn.isEnabled = true
                }

                binding.ownerLoginBtn.setOnClickListener {
                    binding.ownerLoginBtn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
                    binding.userLoginBth.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    binding.userLoginBth.isEnabled = true
                    binding.ownerLoginBtn.isEnabled = false
                }
                loginApi.ownerLogin(data).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        when(response.code()){
                            200 -> {
                                val accessToken = response.headers().get("Authorization").toString()
                                val refreshToken =  response.headers().get("Authorization-refresh").toString()
                                GlobalScope.launch {
                                    userPreferencesRepository.setAccessToken(accessToken)
                                    userPreferencesRepository.setRefreshToken(refreshToken)
                                }

                                startActivity(intent)
                            }
                            else ->{
                                val show = Toast.makeText(context, "아이디와 비밀번호를 확인하세요", Toast.LENGTH_SHORT)
                                    .show();
                                Log.d("로그인 실패", "아이디와 비밀번호를 확인하세요")
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("로그인 통신 실패!", t.message.toString())

                    }
                })

            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_owner_login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_owner_login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

