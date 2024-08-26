package com.example.front

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.front.activity.MainActivity
import com.example.front.databinding.ActivityProfileUpdateBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ProfileUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileUpdateBinding
    private lateinit var apiService: ApiService
    private lateinit var petGenderEditText: EditText
    private lateinit var petNameEditText: EditText
    private lateinit var mainCategorySpinner: Spinner
    private lateinit var subCategorySpinner: Spinner
    private lateinit var petNeuteringEditText: EditText
    private lateinit var petSignificantEditText: EditText
    private lateinit var petAgeEditText: EditText
    private lateinit var petChipNumberEditText: EditText
    private lateinit var petImageEdit: ImageView

    private var petId : Int? = null

    private var selectedMainCategory: String = ""
    private var selectedSubCategory: String = ""

    private var mainCategories = mutableListOf<String>("선택해주세요!", "강아지", "고양이", "직접 입력")
    private val dogBreeds = listOf("선택해주세요!", "그레이하운드", "닥스훈트", "도베르만", "러셀테리어",
        "리트리버", "말티즈", "맬러뮤트", "미니어처핀셔", "베들링턴", "불독", "비숑프리제", "사모예드", "셰퍼드",
        "슈나우저", "시바이누", "시추", "스피츠", "요크셔테리어", "웰시코기", "파피용", "퍼그", "포메라니안", "푸들",
        "허스키", "직접 입력") + "다시 선택"
    private val catBreeds = listOf("선택해주세요!", "노르웨이숲", "러시안블루", "랙돌", "메인쿤", "먼치킨",
        "뱅갈", "브리티쉬숏헤어", "샤미즈", "스코티시폴드", "스핑크스", "아메리칸숏헤어", "아비시니안", "코리안숏헤어",
        "터키쉬앙고라", "페르시안", "직접 입력" ) + "다시 선택"

    private var selectedImageUri: Uri? = null
    private val REQUEST_IMAGE_SELECT = 1
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val context = this@ProfileUpdateActivity
    private val defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/${BuildConfig.FIREBASE_IMAGE_KEY}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation_profile)
        bottomNavigationView.selectedItemId = R.id.profile // 초기 선택된 항목 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())

        initializeViews()
        setupAdapters()
        setupListeners()

        apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)

        val petId = getIntent().getIntExtra("petId", -1)
    }

    private fun initializeViews() {
        mainCategorySpinner = findViewById(R.id.mainCategoryUpdateSpinner)
        subCategorySpinner = findViewById(R.id.subCategoryUpdateSpinner)
        subCategorySpinner.visibility = View.GONE

        petNameEditText = findViewById(R.id.profile_add_name)
        petNeuteringEditText = findViewById(R.id.profile_add_neutering)
        petGenderEditText = findViewById(R.id.profile_add_gender)
        petSignificantEditText = findViewById(R.id.profile_add_significant)
        petAgeEditText = findViewById(R.id.profile_add_age)
        petChipNumberEditText = findViewById(R.id.profile_add_chipNumber)

        val profileUpdateButton = findViewById<Button>(R.id.profile_add_update)
        profileUpdateButton.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri!!) { imageUrl ->
                    petProfileUpdate(imageUrl)
                }
            } else {
                petProfileUpdate(defaultImageUrl)
            }
        }

        // 이미지뷰 클릭하여 이미지가 있으면 이미지 수정, 아니면 이미지 선택해서 불러오는 동작.
        val imageView: ImageView = findViewById(R.id.profile_add_button)
        imageView.setOnClickListener{
            if (selectedImageUri == null) {
                selectImageFromGallery()
            } else {
                showImageOptionsDiaglog()
            }
        }
    }

    // 이미지 갤러리에서 가져오기
    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_SELECT)
    }

    // 이미지가 있는 상태에서 이미지 뷰 클릭 시 변경/제거 옵션
    private fun showImageOptionsDiaglog() {
        val options = arrayOf("이미지 변경", "이미지 제거")
        AlertDialog.Builder(this)
            .setTitle("옵션 선택")
            .setItems(options) { dialog, which ->
                when(which) {
                    0 -> selectImageFromGallery()
                    1 -> removeImage()
                }
            }
            .show()
    }
    private fun removeImage() {
        selectedImageUri = null
        val imageView: ImageView = findViewById(R.id.profile_add_button)
        imageView.setImageResource(0)
        imageView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
    }

    //이미지 firebase storage에 추가하고 추가한 이미지 url 받아오기
    fun uploadImage(imageUri: Uri, callback: (String?) -> Unit) {
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child(fileName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }

    private fun setupAdapters() {
        val mainAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mainCategories)
        mainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mainCategorySpinner.adapter = mainAdapter
        mainCategorySpinner.setSelection(0, false)
    }

    private fun setupListeners() {
        mainCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedMainCategory = mainCategories[position]
                Log.d("SpinnerDebug", "Selected Main Category: $selectedMainCategory")
                if (selectedMainCategory == "직접 입력") {
                    subCategorySpinner.visibility = View.GONE
                    handleOtherCategory("Main Category")
                } else {
                    (view as TextView).text = ""
                    subCategorySpinner.visibility = View.VISIBLE
                    val subCategories = if (selectedMainCategory == "강아지") dogBreeds else catBreeds
                    Log.d("SpinnerDebug", "Calling setupSubCategorySpinner with categories: $subCategories" )
                    setupSubCategorySpinner(subCategories)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        subCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSubCategory = parent.getItemAtPosition(position) as String
                if (selectedSubCategory == "직접 입력") {
                    handleOtherCategory("Sub Category")
                } else if (selectedSubCategory == "다시 선택") {
                    subCategorySpinner.visibility = View.GONE
                    mainCategorySpinner.performClick()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSubCategorySpinner(subCategories: List<String>) {
        val subAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subCategories)
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subCategorySpinner.adapter = subAdapter
        subCategorySpinner.visibility = View.VISIBLE
        Log.d("SpinnerDebug", "subCategorySpinner visibility set to VISIBLE")
    }
    private fun showInputDialog(title: String, onTextEntered: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("확인") { dialog, which ->
            val userInput = input.text.toString()
            onTextEntered(userInput)
            Toast.makeText(this, "입력한 값: $userInput", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("취소") { dialog, which ->
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(BLACK)
//            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(BLACK)
        }
        builder.show()
    }
    private fun convertCategoryToEnglish(category: String): String {
        return when (category) {
            "강아지" -> "DOG"
            "고양이" -> "CAT"
            "직접 입력" -> "P"
            else -> category
        }
    }

    private fun handleOtherCategory(categoryType: String) {
        showInputDialog("$categoryType 입력") { userInput ->
            if (categoryType == "Main Category") {
                selectedMainCategory = "직접 입력"
                selectedSubCategory = userInput
                subCategorySpinner.visibility = View.GONE
                updateMainCategorySpinnerWithUserInput(userInput)
            } else if (categoryType == "Sub Category") {
                val subAdapter = subCategorySpinner.adapter as ArrayAdapter<String>
                subAdapter?.let {
                    val currentPosition = subCategorySpinner.selectedItemPosition
                    it.insert(userInput, currentPosition)
                    selectedSubCategory = userInput
                    it.notifyDataSetChanged()
                    subCategorySpinner.setSelection(currentPosition)
                    Log.e("SpinnerDebug", "SubCategorySpinner adapter is not properly initialized.")
                }
            }
        }
    }

    private fun updateMainCategorySpinnerWithUserInput(userInput: String) {
        val adapter = mainCategorySpinner.adapter as ArrayAdapter<String>
        val position = adapter.getPosition("직접 입력")
        if (position >= 0) {
            mainCategorySpinner.setSelection(position)
            val view = mainCategorySpinner.selectedView as? TextView
            view?.text = userInput
        }
    }

    private fun petProfileUpdate(imageUrl: String?) {
        val petName = petNameEditText.getText().toString().trim()
        val petKind = if (selectedMainCategory == "직접 입력") selectedSubCategory else selectedSubCategory
        val petNeutering = petNeuteringEditText.getText().toString().trim()
        val petGender = petGenderEditText.getText().toString().trim()
        val petSignificant = petSignificantEditText.getText().toString().trim()
        val petCategory =  if (selectedMainCategory == "직접 입력") "P" else convertCategoryToEnglish(selectedMainCategory)
        val petAge = petAgeEditText.getText().toString()
        val petChipNumber = petChipNumberEditText.getText().toString().toLong()

        val imageUrl = imageUrl
        val base64Image = imageUrl

        val petCreateDTO = PetCreateDTO(
            petName,
            petKind,
            petNeutering,
            petGender,
            petSignificant,
            petCategory,
            base64Image,
            petAge,
            petChipNumber
        )

        val petId = getIntent().getIntExtra("petId", -1)
        if (petCreateDTO.petName != null && petCreateDTO.petKind != null && petCreateDTO.petNeutering != null && petCreateDTO.petGender != null && petCreateDTO.petAge != null && petCreateDTO.petCategory != null) {
            apiService.petProfileUpdate(petCreateDTO, petId).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProfileUpdateActivity, "프로필 수정 완료", Toast.LENGTH_SHORT)
                            .show();
                        finish()
                    } else {
                        Toast.makeText(this@ProfileUpdateActivity, "요청 실패!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ProfileUpdateActivity, "network error!", Toast.LENGTH_SHORT)
                        .show();
                }
            })
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