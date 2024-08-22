package com.example.front

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.front.activity.LoginActivity
import com.example.front.data.preferencesRepository
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.api.Authentication
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID


class fragment_profile : Fragment() {
    private lateinit var apiService: ApiService
    private lateinit var textView_user_email_show: TextView
    private lateinit var textView_user_name_show: TextView
    private lateinit var imageView_user: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var petAdapter: PetAdapter
    private var petProfiles: List<PetDTO> = emptyList()
    private lateinit var logout_button: Button
    private lateinit var userProfileSaveButton: Button
    private lateinit var userProfileEditButton: Button
//    private var userId: Int? = null
//    private val userId: Int? =arguments?.getInt("userId")
//    private val id = userId

    //private val context = requireContext()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val REQUEST_IMAGE_SELECT = 1
    private var selectedImageUri: Uri? = null
    private val defaultImageUrl =
        "https://firebasestorage.googleapis.com/v0/b/testing-f501e.appspot.com/o/images%2Fe16ef3a0-7724-4847-a490-d685d22789ce.jpg?alt=media&token=4196f722-af88-4c4d-b815-94ac70aca525"
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val userPreferencesRepository by lazy {
        preferencesRepository.getUserPreferencesRepository(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        recyclerView = view.findViewById(R.id.profile_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(context)
//
//        val userId: Int? = arguments?.getInt("userId")

        Log.d("id","id=$id")

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

        textView_user_name_show = view.findViewById(R.id.textView_user_name_show_profile)
        textView_user_email_show = view.findViewById(R.id.textView_user_email_show)
        imageView_user = view.findViewById(R.id.imageView_user)
        userProfileEditButton = view.findViewById(R.id.userProfileEditButton)
        userProfileSaveButton= view.findViewById(R.id.userProfileSaveButton)

        recyclerView = view.findViewById(R.id.profile_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(context)

        imageView_user.isEnabled = false

//        val userId = arguments?.getInt("userId")
//        Log.d("ProfileFragment", "userId from arguments: $userId")
            loadUser(-1)
            loadPet(-1)


        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    imageView_user.setImageURI(it)
                }
            }
        }

        imageView_user.setOnClickListener {
            if (selectedImageUri == null) {
                selectImageFromGallery()
                imageView_user.setImageURI(selectedImageUri)
            } else {
                showImageOptionsDialog()
            }
        }

        //유저프로필 수정 기능 추가. 24.08.20.
        userProfileEditButton.setOnClickListener {
            enableTextViewEditing(textView_user_name_show)
            enableTextViewEditing(textView_user_email_show)

            userProfileEditButton.visibility = View.GONE
            userProfileSaveButton.visibility = View.VISIBLE

            imageView_user.isEnabled = true

        }


        userProfileSaveButton.setOnClickListener {
            showSaveConfirmationDialog()
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
            val token : String = ""
            lifecycleScope.launch {
                userPreferencesRepository.setAccessToken(token)
                userPreferencesRepository.setRefreshToken(token)
                startActivity(intent)

            }
        }

        //FirebaseApp 실행 전 초기화.
        FirebaseApp.initializeApp(requireContext())

    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }

    private fun removeImage() {
        selectedImageUri = null
        imageView_user.setImageResource(0)
        imageView_user.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
    }

    private fun showImageOptionsDialog() {
        val options = arrayOf("이미지 변경", "이미지 제거")
        AlertDialog.Builder(requireContext())
            .setTitle("옵션 선택")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> selectImageFromGallery()
                    1 -> removeImage()
                }
            }
            .show()
    }

    private fun enableTextViewEditing(textView: TextView) {
        textView.setOnClickListener {
            // AlertDialog를 사용하여 텍스트 수정하기
            val builder = AlertDialog.Builder(context)
            builder.setTitle("프로필 수정하기")

            // EditText를 다이얼로그에 추가
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setText(textView.text.toString())
            builder.setView(input)

            // "저장" 버튼 클릭 시 TextView의 텍스트 업데이트
            builder.setPositiveButton("저장") { _, _ ->
                textView.text = input.text.toString()

                imageView_user.isEnabled = false
            }

            // "취소" 버튼 클릭 시 다이얼로그 닫기
            builder.setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }
    }

    private fun showSaveConfirmationDialog() {
        // "저장하시겠습니까?" 다이얼로그 표시
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("프로필(사용자) 수정")
        builder.setMessage("이대로 수정하시겠습니까?")

        // "Yes" 버튼 클릭 시 실제 데이터 저장 로직
        builder.setPositiveButton("Yes") { _, _ ->
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri!!) { imageUrl ->
                    saveUserData(imageUrl!!)
                }
            } else {
                saveUserData(defaultImageUrl)
            }

            // '저장' 버튼 숨기고 '수정' 버튼 다시 보이기
            userProfileSaveButton.visibility = View.GONE
            userProfileEditButton.visibility = View.VISIBLE
        }

        // "No" 버튼 클릭 시 다이얼로그 닫기
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun uploadImage(imageUri: Uri, callback: (String?) -> Unit) {
        val fileName = "profileImages/${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child(fileName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }

    private fun saveUserData(imageUrl: String) {
        // 수정된 프로필 정보 db에 저장
        val nickname = textView_user_name_show.text.toString()
        val email = textView_user_email_show.text.toString()
        val userDTO = UserDTO(nickname, email, imageUrl)
//        val userId = arguments?.getInt("userId")
//        val id = userId

//        Log.d("id","id=$id")

        apiService.editUserProfile(userDTO, id).enqueue(object :
            Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>){
                if(response.isSuccessful) {
                    showToast("프로필을 성공적으로 수정하였습니다!")
                    Log.d("nickname","nickname=$nickname")
                    Log.d("email","email=$email")
                    Log.d("imageUrl","imageUrl=$imageUrl")
                } else {
                    showToast("프로필 수정에 문제가 발생했습니다!")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("인터넷 연결에 문제가 발생했습니다!")
            }
        })
    }

    private fun navigateToProfileAddActivity(userId: Int?) {
//        val userId = arguments?.getInt("userId")
//        Log.d("userIdBeforeAdd?", "UserId = $userId")
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
                        Log.d("user.nickname", "user.nickname=${user.nickname}")
                        Log.d("user.email", "user.email=${user.email}")
                        textView_user_name_show.text = user.nickname
                        textView_user_email_show.text = user.email
                        val imageUrl = user.imageUrl
                        Log.d("user.imageUrl","user.imageUrl=${user.imageUrl}")
                        Glide.with(imageView_user.context)
                            .load(imageUrl)
                            .placeholder(R.drawable.anibuddy_logo) // 로딩 중 표시할 이미지
                            .error(R.drawable.anibuddy_logo) // 로드 실패 시 표시할 이미지
                            .into(imageView_user) // 이미지를 로드할 ImageView
//                        Log.d("imageUrl","imageUrl=$imageUrl")
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

    // 프로필 추가되면 새로고침
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PROFILE_DETAIL && resultCode == Activity.RESULT_OK) {
            val refreshProfileFragment =
                data?.getBooleanExtra("refreshProfileFragment", false) ?: false
//            val userId: Int? = arguments?.getInt("userId")
            if (refreshProfileFragment) {
                loadUser(id) // 데이터 새로 고침
                loadPet(id)
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

    // fragment_profile 갱신
    override fun onResume() {
        super.onResume()
//        val userId = arguments?.getInt("userId")
//        Log.d("ProfileFragmentReset", "userId from arguments: $userId")
//        if (userId != null) {
            loadUser(id)
            loadPet(id)
//        }
    }
}