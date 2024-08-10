package com.example.front

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.front.data.LoginApiService
import com.example.front.data.request.checkEmail
import com.example.front.databinding.FragmentSignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_signup.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_signup : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var bundle = Bundle()
    private lateinit var binding: FragmentSignupBinding

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
        val api = LoginApiService.create()
        val binding= FragmentSignupBinding.inflate(inflater,container,false)
        if(bundle.getString("key").toString().equals("USER")){
            binding.signup.text = "사용자 회원가입"
        } else if(bundle.getString("key").toString().equals("OWNER")){
            binding.signup.text = "사업자 회원가입"
        }
        binding.emailcheckbutton.setOnClickListener{
            val email = binding.signup.text.toString()
            val ob = checkEmail(email)
            api.checkEmail(ob).enqueue(object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.d("우와와와와와오", response.code().toString())
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("dndhkdhdhkdh","twe")
                }

            })
        }
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
         * @return A new instance of fragment fragment_signup.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_signup().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}