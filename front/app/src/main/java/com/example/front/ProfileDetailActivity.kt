package com.example.front

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.front.activity.MainActivity
import com.example.front.databinding.ActivityProfileDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileDetailBinding
    private lateinit var apiService: ApiService
    private lateinit var profileDetailRemoveButton: Button
    private lateinit var profileDetailEditButton: Button
    private lateinit var profileDetailGender: TextView
    private lateinit var profileDetailName: TextView
    private lateinit var profileDetailNeutering: TextView
    private lateinit var profileDetailAge: TextView
    private lateinit var profileDetailKind: TextView
    private lateinit var profileDetailChipNumber: TextView
    private lateinit var profileDetailSignificant: TextView
    private lateinit var profileDetailImage: ImageView
    private var petId: Int? = null
    private val context = this@ProfileDetailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation_profile)
        bottomNavigationView.selectedItemId = R.id.profile // 초기 선택된 항목 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())

        apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)

        val petId = getIntent().getIntExtra("petId", -1)
        Log.d("ProfileDetailActivity", "petId from intent: $petId")
        if (petId != -1) {
            profileDetailName = findViewById(R.id.profile_detail_name)
            profileDetailGender = findViewById(R.id.profile_detail_gender)
            profileDetailKind = findViewById(R.id.profile_detail_kind)
            profileDetailNeutering = findViewById(R.id.profile_detail_neutering)
            profileDetailAge = findViewById(R.id.profile_detail_age)
            profileDetailChipNumber = findViewById(R.id.profile_detail_chipNumber)
            profileDetailSignificant = findViewById(R.id.profile_detail_significant)
            profileDetailImage = findViewById(R.id.profile_detail_image)
            loadPetDetail(petId)
        } else {
            showToast("해당 프로필을 찾을 수 없습니다!")
        }

        val profileDetailRemoveButton = findViewById<Button>(R.id.profile_detail_remove)
        profileDetailRemoveButton.setOnClickListener {
            petProfileDelete(petId)
        }

        val profileDetailEditButton = findViewById<Button>(R.id.profile_detail_edit)
        profileDetailEditButton.setOnClickListener {
            val petId = getIntent().getIntExtra("petId", -1)
            Log.d("PDA petID?", "PetId = $petId")
            val intent = Intent(this, ProfileUpdateActivity::class.java)
            intent.putExtra("petId", petId)
            startActivity(intent)
        }
    }


    private fun loadPetDetail(petId: Int) {
        apiService.getProfileDetailByPetId(petId).enqueue(object : Callback<PetDetailDTO> {
            override fun onResponse(call: Call<PetDetailDTO>, response: Response<PetDetailDTO>) {
                Log.d("ProfileFragment", "API Response Code: ${response.code()}")
                Log.d("ProfileFragment", "Response Body: ${response.body()}")
                Log.d("ProfileFragment", "Response Error Body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    response.body()?.let { petDetail ->
                        profileDetailName.text = petDetail.petName
                        profileDetailGender.text = petDetail.petGender
                        profileDetailKind.text = petDetail.petKind
                        profileDetailNeutering.text = petDetail.petNeutering
                        profileDetailAge.text = petDetail.petAge.toString()
                        profileDetailChipNumber.text = petDetail.petChipNumber.toString()
                        profileDetailSignificant.text = petDetail.petSignificant
                        Glide.with(profileDetailImage.context)
                            .load(petDetail.base64Image)
                            .placeholder(R.drawable.anibuddy_logo) // 로딩 중 표시할 이미지
                            .error(R.drawable.anibuddy_logo) // 로드 실패 시 표시할 이미지
                            .into(profileDetailImage) // 이미지를 로드할 ImageView
                    } ?: run {
                        Log.d("ProfileFragment", "Response body is null")
                    }
                } else {
                    Log.d(
                        "ProfileFragment",
                        "Response not successful: ${response.errorBody()?.string()}"
                    )
                    showToast("요청 실패!")
                }
            }

            override fun onFailure(call: Call<PetDetailDTO>, t: Throwable) {
                Log.e("ProfileFragment", "API Failure: ${t.message}")
                showToast("네트워크 연결 에러!")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun petProfileDelete(petId: Int) {
        AlertDialog.Builder(this)
            .setTitle("삭제 확인")
            .setMessage("정말 삭제하시겠습니까?")
            .setPositiveButton("확인") { dialog, _ ->
                apiService.deleteProfile(petId).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            showToast("프로필이 삭제되었습니다.")
                            dialog.dismiss()
                            val intent = Intent()
                            intent.putExtra("refreshProfileFragment", true)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            showToast("프로필 삭제 실패!")
                            Log.d(
                                "ProfileDetailActivity",
                                "Delete response not successful: ${response.errorBody()?.string()}"
                            )
                        }
                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        showToast("네트워크 연결 에러!")
                        Log.e("ProfileDetailActivity", "API Failure: ${t.message}")
                        dialog.dismiss()
                    }
                })
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss() // 대화 상자 닫기
            }
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        val petId = getIntent().getIntExtra("petId", -1)
        if (petId != -1) {
            loadPetDetail(petId)
        }
    }


    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.home -> navigateToMainActivity(MainActivity.DESTINATION_HOME)
                R.id.reservationList -> navigateToMainActivity(MainActivity.DESTINATION_RESERVATION_LIST)
                R.id.follwingList -> navigateToMainActivity(MainActivity.DESTINATION_FOLLOWING_LIST)
                R.id.chatList -> navigateToMainActivity(MainActivity.DESTINATION_CHAT_LIST)
                R.id.profile -> navigateToMainActivity(MainActivity.DESTINATION_PROFILE)
            }
            return true
        }
    }

    private fun navigateToMainActivity(destination: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_DESTINATION, destination)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish() // ProfileAddActivity 종료
    }
}