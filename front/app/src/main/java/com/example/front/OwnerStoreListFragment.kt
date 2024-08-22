package com.example.front

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.replace
import com.bumptech.glide.Glide
import com.example.front.activity.OwenerMainActivity
import com.example.front.activity.OwnerStoreDetailActivity
import com.example.front.data.OwnerApiService
import com.example.front.data.response.OwnerStoreListResponse
import com.example.front.databinding.FragmentOwnerStoreListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OwnerStoreListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OwnerStoreListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentOwnerStoreListBinding
    private val StoreAddFragment : Fragment = StoreAddFragment()

    private lateinit var apiService: ApiService
    private lateinit var textView_user_email_show: TextView
    private lateinit var textView_user_name_show: TextView
    private lateinit var imageView_user : ImageView


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
        // Inflate the layout for this fragment
        binding = FragmentOwnerStoreListBinding.inflate(inflater,container,false)
        textView_user_name_show = binding.textViewUserNameShowProfile
        textView_user_email_show = binding.textViewUserEmailShow
        val contenxt = requireContext()

        val api = OwnerApiService.create(requireContext())
        imageView_user = binding.imageViewUser

        apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)
        loadUser(1)


        binding.addStore.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.owner_main,StoreAddFragment).addToBackStack(null).commitAllowingStateLoss()
        }

        api.getOwnerStoreList().enqueue(object : Callback<List<OwnerStoreListResponse>>{
            override fun onResponse(
                call: Call<List<OwnerStoreListResponse>>,
                response: Response<List<OwnerStoreListResponse>>
            ) {
                val storeList = response.body()
                for(store: OwnerStoreListResponse in storeList!!){
                    val button = androidx.appcompat.widget.AppCompatButton(contenxt)
                    setAddView(button,store)
                }
            }

            override fun onFailure(call: Call<List<OwnerStoreListResponse>>, t: Throwable) {
                Log.d("서버 오류", "오류오류")
            }

        })

        return binding.root
    }

    fun setAddView(tv: androidx.appcompat.widget.AppCompatButton, store: OwnerStoreListResponse) {
        (tv.parent as ViewGroup?)?.removeView(tv)
        tv.text = store.storeName + " \n " + store.storeAddress

        // tv의 레이아웃 파라미터 설정
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // 너비를 match_parent로 설정
            ViewGroup.LayoutParams.WRAP_CONTENT // 높이를 60dp로 설정 (dp를 px로 변환해야 함)
        ).apply {
            setMargins(20.dp, 20.dp, 20.dp, 20.dp) // 여백을 20dp로 설정
            Gravity.CENTER // 레이아웃의 중심에 정렬
        }

        tv.layoutParams = layoutParams

        // 텍스트 속성 설정
        tv.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF8A00")))
        tv.setBackgroundColor(Color.parseColor("#FFFFFF"))
        tv.textSize = 16f // textSize는 sp 단위로 설정
        tv.setTypeface(null, Typeface.BOLD) // 텍스트 스타일을 Bold로 설정
        tv.setOnClickListener{
            Log.d("가게가게 이름 이름", store.storeName + " : " + store.id)
            val intent = Intent(requireContext(), OwnerStoreDetailActivity::class.java)
            intent.putExtra("storeId",store.id)
            startActivity(intent)
        }
        binding.ownerStoreList.addView(tv)
    }

    private fun loadUser(userId: Int) {
        apiService.getProfileAboutUser(userId).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        textView_user_name_show.text = user.nickname
                        textView_user_email_show.text = user.email
                        Glide.with(imageView_user.context)
                            .load(user.imageUrl)
                            .placeholder(R.drawable.anibuddy_logo) // 로딩 중 표시할 이미지
                            .error(R.drawable.anibuddy_logo) // 로드 실패 시 표시할 이미지
                            .into(imageView_user) // 이미지를 로드할 ImageView
                    }
                }
                // 응답 없음
                else {
                    showToast("요청 실패!")
                }
            }

            // 요청 실패
            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                showToast("네트워크 연결 에러!")
            }
        })

    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OwnerStoreListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OwnerStoreListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}