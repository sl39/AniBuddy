package com.example.front

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.front.activity.LoginActivity
import com.example.front.data.LoginApiService
import com.example.front.data.request.CheckEmail
import com.example.front.data.request.CheckNickname
import com.example.front.data.request.signupRequest
import com.example.front.databinding.FragmentSignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        var em = false
        var nick = false
        var password = false
        val role = bundle.getString("key").toString()


        val api = LoginApiService.create()
        val binding= FragmentSignupBinding.inflate(inflater,container,false)
        if(role.equals("USER")){
            binding.signup.text = "사용자 회원가입"
        } else if(role.equals("OWNER")){
            binding.signup.text = "사업자 회원가입"
        }


        // email check
        binding.signmail.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                val email = binding.signmail.text.toString().replace(" ","")
                if(email == ""){
                    em = false
                    binding.emailtext.text = "이메일을 입력해 주세요"
                    binding.emailtext.setTextColor(ColorStateList.valueOf(Color.parseColor("#D9D9D9")))
                    return@setOnFocusChangeListener
                }
                val os = CheckEmail(email)
                api.checkEmail(os).enqueue(object : Callback<Boolean>{
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.body() == true){
                            em = false
                            binding.emailtext.text = "사용하실수 없는 아이디 입니다."
                            binding.emailtext.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF0000")))
                        } else {
                            binding.emailtext.text = "사용 가능한 아이디 입니다."
                            binding.emailtext.setTextColor(ColorStateList.valueOf(Color.parseColor("#0000ff")))
                            em = true
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        em = false
                        binding.emailtext.text = "사용하실수 없는 아이디 입니다."
                        binding.emailtext.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF0000")))


                    }

                })
            }
        }

        // nickName check
        binding.signID.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                val email = binding.signID.text.toString().replace(" ","")
                if(email == ""){
                    binding.nicknametext.text = "닉네임을 입력해 주세요"
                    binding.nicknametext.setTextColor(ColorStateList.valueOf(Color.parseColor("#D9D9D9")))
                    nick = false
                    return@setOnFocusChangeListener
                }
                val os = CheckNickname(email)
                api.checkNickname(os).enqueue(object : Callback<Boolean>{
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.body() == true){
                            nick = false
                            binding.nicknametext.text = "사용하실수 없는 닉네임 입니다."
                            binding.nicknametext.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF0000")))
                        } else {
                            nick = true
                            binding.nicknametext.text = "사용가능한 닉네임 입니다."
                            binding.nicknametext.setTextColor(ColorStateList.valueOf(Color.parseColor("#0000FF")))
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        nick = false
                        binding.nicknametext.text = "사용하실수 없는 닉네임 입니다."
                        binding.nicknametext.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF0000")))
                    }

                })
            }
        }
        
        // 비밀번호 체크
        binding.signPW.setOnFocusChangeListener{ v, hasFocus ->
            if(!hasFocus){
                if (binding.signPW.text.equals("")){
                    binding.paaswordtext.text = "비밀번호를 입력해 주세요"
                    binding.paaswordtext.setTextColor(ColorStateList.valueOf(Color.parseColor("#D9D9D9")))
                    binding.paaswordtext.visibility = VISIBLE
                    password = false
                    return@setOnFocusChangeListener
                }
                binding.paaswordtext.visibility = INVISIBLE
            }
        }
        binding.signPW2.addTextChangedListener{
            Log.d("password 확인", binding.signPW.text.toString() + " : " + binding.signPW2.text.toString())
            if(binding.signPW.text.equals("")){
                binding.paaswordtext2.text = "비밀번호를 입력해 주세요"
                binding.paaswordtext2.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF0000")))
                password = false
                return@addTextChangedListener
            }
            if(binding.signPW.text.toString().equals(binding.signPW2.text.toString())){
                binding.paaswordtext2.text = "비밀번호가 동일합니다!"
                binding.paaswordtext2.setTextColor(ColorStateList.valueOf(Color.parseColor("#0000FF")))
                password = true
            } else {
                binding.paaswordtext2.text = "비밀번호가 다릅니다!"
                binding.paaswordtext2.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF0000")))
                password = false
            }
        }

        binding.signupbutton.setOnClickListener{
            if(!em){
                val toast =
                    Toast.makeText(requireContext(), "이메일을 확인해 주세요", Toast.LENGTH_SHORT)
                toast.show()
                return@setOnClickListener
            }
            if(!nick) {
                val toast =
                    Toast.makeText(requireContext(), "닉네임을 확인해 주세요", Toast.LENGTH_SHORT)
                toast.show()
                return@setOnClickListener
            }
            if(!password){
                val toast =
                    Toast.makeText(requireContext(), "비밀번호를 확인해 주세요", Toast.LENGTH_SHORT)
                toast.show()
                return@setOnClickListener
            }
            val signupNickname = binding.signID.text.toString()
            val signupEmail = binding.signmail.text.toString()
            val signupPassword = binding.signPW.text.toString()
            val signupPassword2 = binding.signPW2.text.toString()
            val data = signupRequest(signupNickname,signupEmail,signupPassword,signupPassword2,signupNickname,role)
            api.signup(data).enqueue(object : Callback<CheckEmail>{
                override fun onResponse(call: Call<CheckEmail>, response: Response<CheckEmail>) {
                    if(response.code() == 200){
                        val toast = Toast.makeText(requireContext(), "회원가입에 성공했습니다! 로그인해주세요!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(),LoginActivity::class.java)
                        startActivity(intent)
                    }

                }

                override fun onFailure(call: Call<CheckEmail>, t: Throwable) {
                    Log.d("실패ㅠㅜㅠㅜㅠㅜ!!", "샐패")
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