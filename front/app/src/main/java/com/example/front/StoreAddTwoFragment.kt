package com.example.front

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.front.data.LocationApiService
import com.example.front.data.response.LocationResponse
import com.example.front.databinding.FragmentStoreAddTwoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreAddTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreAddTwoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentStoreAddTwoBinding
    var bundle = Bundle()

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
        val dayday = mutableListOf<String>()
        val address = bundle.getString("lnmAdres")
        val roadAddress = bundle.getString("rnAdres")
        val detailAddress = bundle.getString("detailAddress")
        val info = ""
        val storeImageList = mutableListOf<String>()
        val name = bundle.getString("name")
        val phone_number = bundle.getString("phone_number")
        val category = bundle.getStringArrayList("category")
        binding = FragmentStoreAddTwoBinding.inflate(inflater,container,false)

//        binding.hour.filters = arrayOf(InputFilterMinMax(0,23))
//        binding.minutes.filters = arrayOf(InputFilterMinMax(0,59))
//        binding.endHour.filters = arrayOf(InputFilterMinMax(0,23))
//        binding.endMinutes.filters = arrayOf(InputFilterMinMax(0,59))

        val dayBtns = binding.dayLinear
        for(i in 0 .. dayBtns.childCount){
            val view = dayBtns.getChildAt(i)
            if(view is androidx.appcompat.widget.AppCompatButton){
                view.setOnClickListener{
                    val txt = view.text.toString()
                        if(txt in dayday) {
                        view.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
                        dayday.remove(txt)
                    } else {
                        view.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#FF8A00"))
                        dayday.add(txt)
                    }

                }
            }
        }


        binding.addStoreButton.setOnClickListener{
            val d = charArrayOf('일','월','화','수','목','금','토')
            var openDay = ""
            val hour = binding.hour.text.toString()
            val minutes = binding.minutes.text.toString()
            val endHour = binding.endHour.text.toString()
            val endMinutes = binding.endMinutes.text.toString()
            var district = ""
            if(hour=="" || minutes=="" || endHour=="" || endMinutes==""){

                return@setOnClickListener
            }
            if (dayday.size == 0){
                return@setOnClickListener
            }
            val tiempo = hour + ":" + minutes + " - " + endHour + ":" + endMinutes
            for(s: Char in d){
                for(j: String in dayday){
                    if(s.toString().equals(j)){
                        openDay += j + " " + tiempo + "//"
                    }
                }
            }
            val guArray = arrayOf(
                "북구",
                "남구",
                "동래구",
                "금정구",
                "사상구",
                "부산진구",
                "강서구",
                "해운대구",
                "동구",
                "영도구",
                "연제구",
                "사하구",
                "중구",
                "수영구",
                "기장군",
                " 서구"
            )
            for(dst : String in guArray){
                if(address?.contains(dst) == true){
                    district = dst
                    break
                }
            }

            val api = LocationApiService.create()
            if(address != null){
                Log.d("ㅂㅈㄷㄱㅂㅈㄷㄱㅂㅈㄷㄱㄷㅈ","ㅂㄷㅈㄱㅂㅈㄷㄱㅂㅈㄷㄱ")
                api.getLocation("KakaoAK 5ad4598a787c971bf6290233c6bcbfc0",address).enqueue(object : Callback<LocationResponse> {
                    override fun onResponse(call: Call<LocationResponse>, response: Response<LocationResponse>) {
                        Log.d("값이 들어오는지 확인", response.body().toString())
                    }

                    override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                        Log.d(".테스트" , "실패 실패")
                    }
                })

            }



            Log.d("가게 등록 요소 요소 요소"," openDay : " + openDay + " district : " + district + " address : "+ address +" ${detailAddress}"+ " roadAddress : " + roadAddress +" ${detailAddress}"+ " info : " + info  +" storeImageList :"+ storeImageList.toString() + " name : " + name + " phone_number  : " + phone_number  +  " category : " + category .toString() )
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
         * @return A new instance of fragment StoreAddTwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoreAddTwoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
