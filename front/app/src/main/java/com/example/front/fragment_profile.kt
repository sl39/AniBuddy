package com.example.front

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.front.activity.LoginActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class fragment_profile : Fragment() {
    private lateinit var apiService: ApiService
    private lateinit var textView_user_email_show: TextView
    private lateinit var textView_user_name_show: TextView
    private lateinit var imageView_user : ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var petAdapter: PetAdapter
    private var petProfiles: List<PetDTO> = emptyList()
    private lateinit var logout_button: Button
    private var userId: Int? = null
    //private val context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        recyclerView = view.findViewById(R.id.profile_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val userId: Int? = arguments?.getInt("userId")

        petAdapter = PetAdapter(petProfiles) { profile ->
            val intent = Intent(context, ProfileDetailActivity::class.java)
            intent.putExtra("petId", profile.petId)
            startActivity(intent)
        }

        recyclerView.adapter = petAdapter
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView_user_name_show = view.findViewById(R.id.textView_user_name_show)
        textView_user_email_show = view.findViewById(R.id.textView_user_email_show)
        imageView_user = view.findViewById(R.id.imageView_user)

        recyclerView = view.findViewById(R.id.profile_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val userId = arguments?.getInt("userId")
        Log.d("ProfileFragment", "userId from arguments: $userId")
        if (userId != null) {
            loadUser(userId)
            loadPet(userId)
        } else {
            showToast("유저를 찾을 수 없습니다!")
        }

        //FAB에 모양 추가해주기 위해 EFAB로 변경함. 24.08.15.
        val fab: ExtendedFloatingActionButton = view.findViewById(R.id.button_to_profile_add)
        fab.setOnClickListener        {
            navigateToProfileAddActivity(id)
        }

        // 로그아웃 버튼 구현 24.08.15.
        val logout = view.findViewById<Button>(R.id.logout_button)
        logout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.putExtra("AccessToken", "")
            intent.putExtra("RefreshToken", "")
            startActivity(intent)
        }

        FirebaseApp.initializeApp(requireContext())
    }

    private fun navigateToProfileAddActivity(userId: Int?) {
        val userId = arguments?.getInt("userId")
        Log.d("userIdBeforeAdd?", "UserId = $userId")
        userId?.let { id ->
            val intent = Intent(requireContext(), ProfileAddActivity::class.java)
            intent.putExtra("userId", id)
            startActivity(intent)
        }
    }

    private fun loadPet(userId: Int) {
        apiService.getPetProfileByUserId(userId).enqueue(object : Callback<List<PetDTO>> {
            override fun onResponse(call: Call<List<PetDTO>>, response: Response<List<PetDTO>>) {
                if (response.isSuccessful) {
                    petProfiles = response.body() ?: emptyList()
                    petAdapter = PetAdapter(petProfiles) { petDTO ->
                        val intent = Intent(context, ProfileDetailActivity::class.java).apply {
                            putExtra("petId", petDTO.petId)
                        }
                        startActivity(intent)
                    }
                    recyclerView.adapter = petAdapter
                } else {
                    showToast("요청 실패!")
                }
            }

            override fun onFailure(call: Call<List<PetDTO>>, t: Throwable) {
                showToast("네트워크 연결 에러!")
            }
        })
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

    // profileAddActivity에서 프로필 추가되면 새로고침
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PROFILE_DETAIL && resultCode == Activity.RESULT_OK) {
            val refreshProfileFragment =
                data?.getBooleanExtra("refreshProfileFragment", false) ?: false
            val userId: Int? = arguments?.getInt("userId")
            if (refreshProfileFragment) {
                loadUser(userId!!) // 데이터 새로 고침
                loadPet(userId)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PROFILE_DETAIL = 1
    }

    //Toast메세지 띄우기.
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // userId가 존재하면 UserProfile과 연결된 petProfile 갱신.
    override fun onResume() {
        super.onResume()
        val userId = arguments?.getInt("userId")
        Log.d("ProfileFragmentReset", "userId from arguments: $userId")
        if (userId != null) {
            loadUser(userId)
            loadPet(userId)
        }
    }
}