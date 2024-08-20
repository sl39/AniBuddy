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
import com.example.front.databinding.ActivityProfileAddBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID


class ProfileAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAddBinding
    private lateinit var apiService: ApiService
    private lateinit var petGenderEditText: EditText
    private lateinit var petNameEditText: EditText
    private lateinit var mainCategorySpinner: Spinner
    private lateinit var subCategorySpinner: Spinner
    private lateinit var petNeuteringEditText: EditText
    private lateinit var petSignificantEditText: EditText
    private lateinit var petAgeEditText: EditText
    private lateinit var petChipNumberEditText: EditText
    private lateinit var imageView: ImageView

    private val context = this@ProfileAddActivity

    private val defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/testing-f501e.appspot.com/o/images%2Fe16ef3a0-7724-4847-a490-d685d22789ce.jpg?alt=media&token=4196f722-af88-4c4d-b815-94ac70aca525"

    private var userId: Int? = null

    private var selectedMainCategory: String = ""
    private var selectedSubCategory: String = ""

    private var mainCategories = mutableListOf<String>("선택해주세요!", "강아지", "고양이", "그 외")
    private val dogBreeds = listOf("선택해주세요!", "그레이하운드", "닥스훈트", "도베르만", "러셀테리어",
    "리트리버", "말티즈", "맬러뮤트", "미니어처핀셔", "베들링턴", "불독", "비숑프리제", "사모예드", "셰퍼드",
    "슈나우저", "시바이누", "시추", "스피츠", "요크셔테리어", "웰시코기", "파피용", "퍼그", "포메라니안", "푸들",
    "허스키", "그 외") + "다시 선택"
    private val catBreeds = listOf("선택해주세요!", "노르웨이숲", "러시안블루", "랙돌", "메인쿤", "먼치킨",
        "뱅갈", "브리티쉬숏헤어", "샤미즈", "스코티시폴드", "스핑크스", "아메리칸숏헤어", "아비시니안", "코리안숏헤어",
        "터키쉬앙고라", "페르시안", "그 외" ) + "다시 선택"

    private val REQUEST_IMAGE_SELECT = 1
    private var selectedImageUri: Uri? = null
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAddBinding.inflate(layoutInflater)
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

    }

    private fun initializeViews() {
        mainCategorySpinner = findViewById(R.id.mainCategorySpinner)
        subCategorySpinner = findViewById(R.id.subCategorySpinner)
        subCategorySpinner.visibility = View.GONE
        imageView = findViewById(R.id.profile_add_image)

        petNameEditText = findViewById(R.id.profile_add_name)
        petNeuteringEditText = findViewById(R.id.profile_add_neutering)
        petGenderEditText = findViewById(R.id.profile_add_gender)
        petSignificantEditText = findViewById(R.id.profile_add_significant)
        petAgeEditText = findViewById(R.id.profile_add_age)
        petChipNumberEditText = findViewById(R.id.profile_add_chipNumber)

        val profileRegistrationButton = findViewById<Button>(R.id.profile_add_registration)
        profileRegistrationButton.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri!!) { imageUrl ->
                    petProfileRegistration(imageUrl)
                }
            } else {
                petProfileRegistration(defaultImageUrl)
            }
        }

        val imageView: ImageView = findViewById(R.id.profile_add_image)
        imageView.setOnClickListener{
            if (selectedImageUri == null) {
                selectImageFromGallery()
            } else {
                showImageOptionsDiaglog()
            }
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
                if (selectedMainCategory == "그 외") {
                    subCategorySpinner.visibility = View.GONE
                    handleOtherCategory("Main Category")
                } else {
                    (view as TextView).text = ""
                    subCategorySpinner.visibility = View.VISIBLE
                    val subCategories = if (selectedMainCategory == "강아지") dogBreeds else catBreeds
                    setupSubCategorySpinner(subCategories)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        subCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedSubCategory = parent.getItemAtPosition(position) as String
                if (selectedSubCategory == "그 외") {
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
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                imageView.setImageURI(it)
            }
        }
    }

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


    //이미지 업로드 메소드
    private fun uploadImage(imageUri: Uri, callback: (String?) -> Unit) {
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

    private fun handleOtherCategory(categoryType: String) {
        showInputDialog("$categoryType 입력") { userInput ->
            if (categoryType == "Main Category") {
                selectedMainCategory = "그 외"
                selectedSubCategory = userInput
                subCategorySpinner.visibility = View.GONE
                updateMainCategorySpinnerWithUserInput(userInput)
            } else if (categoryType == "Sub Category") {
                val subAdapter = subCategorySpinner.adapter as ArrayAdapter<String>
                subAdapter?.let {
                    val currentPosition = subCategorySpinner.selectedItemPosition
                    it.insert(userInput, currentPosition)
                    selectedSubCategory = userInput // Update the selected sub category
                    it.notifyDataSetChanged()
                    subCategorySpinner.setSelection(currentPosition)
                }
            }
        }
    }

    private fun updateMainCategorySpinnerWithUserInput(userInput: String) {
        val adapter = mainCategorySpinner.adapter as ArrayAdapter<String>
        val position = adapter.getPosition("그 외")
        if (position >= 0) {
            mainCategorySpinner.setSelection(position)
            val view = mainCategorySpinner.selectedView as? TextView
            view?.text = userInput
        }
    }

    private fun showInputDialog(title: String, onTextEntered: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("확인") { dialog, which ->
            val userInput = input.text.toString()
            onTextEntered(userInput)
//            Toast.makeText(this, "입력한 값: $userInput", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("취소") { dialog, which ->
            dialog.cancel()
        }
//        val dialog = builder.create()
//        dialog.setOnShowListener {
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(BLACK)
//            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(BLACK)
//        }
        builder.show()
    }

    private fun convertCategoryToEnglish(category: String): String {
        return when (category) {
            "강아지" -> "DOG"
            "고양이" -> "CAT"
            "그 외" -> "P"
            else -> category // 기본값 반환
        }
    }

    private fun petProfileRegistration(imageUrl: String?) {
        val petName = petNameEditText.getText().toString().trim()
        val petKind =
            if (selectedMainCategory == "그 외") selectedSubCategory else selectedSubCategory
        val petNeutering = petNeuteringEditText.getText().toString().trim()
        val petGender = petGenderEditText.getText().toString().trim()
        val petSignificant = petSignificantEditText.getText().toString().trim()
        val petCategory = if (selectedMainCategory == "그 외") "P" else convertCategoryToEnglish(
            selectedMainCategory
        )
        val petAge = petAgeEditText.getText().toString().toInt()
        val petChipNumber = petChipNumberEditText.getText().toString().toLong()

        val imageUrl = imageUrl
        val base64Image = imageUrl

//        Log.d("PAA_Base64Image", "Base64 Image: $base64Image")
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

        val userId = intent.getIntExtra("userId", -1)
        Log.d("userIdprofileAddActivity", "userId = $userId")
//        if (base64Image.isNotEmpty()) {
//            apiService.petProfileRegistration(petCreateDTO, userId).enqueue(object :
//                Callback<UserResponse> {
        apiService.petProfileRegistration(petCreateDTO, userId).enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ProfileAddActivity,
                        "프로필 추가 완료: ${response.body()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(this@ProfileAddActivity, "요청 실패!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProfileAddActivity,
                    "network error! : ${t.message}",
                    Toast.LENGTH_SHORT
                ).show();
            }
        })
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











    /*


            bitmap
        }
//        if (bitmap == null) {
//            Toast.makeText(this, "Default image not found", Toast.LENGTH_SHORT).show()
//            return ""
//        }
        return if (bitmap != null) {
            encodeImageToBase64(bitmap)
        } else {
            ""
        }
    }

    // 이미지 Base64로 인코딩
    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}



//    private fun handleOtherCategory(categoryType: String, position: Int) {
//        showInputDialog(categoryType, position) { userInput ->
//            if (categoryType == "Main Category") {
//                mainCategories[position] = userInput
//                selectedMainCategory = userInput
//                (mainCategorySpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
//                mainCategorySpinner.setSelection(position)
//            } else if (categoryType == "Sub Category") {
//                val subAdapter = subCategorySpinner.adapter as ArrayAdapter<String>
//                subAdapter?.let {
//                    it.remove(it.getItem(position))
//                    it.insert(userInput, position)
//                    selectedSubCategory = userInput // Update the selected sub category
//                    it.notifyDataSetChanged()
//                    subCategorySpinner.setSelection(position)
//                    Log.e("SpinnerDebug", "SubCategorySpinner adapter is not properly initialized.")
//                }
//            }
//        }
//    }



=------------===================코드 최적화해달라고 말함===============================================
class ProfileAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAddBinding
    private lateinit var apiService: ApiService
    private lateinit var petGenderEditText: EditText
    private lateinit var petNameEditText: EditText
    private lateinit var mainCategorySpinner: Spinner
    private lateinit var subCategorySpinner: Spinner
    private lateinit var petNeuteringEditText: EditText
    private lateinit var petSignificantEditText: EditText
    private lateinit var petAgeEditText: EditText
    private lateinit var petChipNumberEditText: EditText
    private val userId: Int = 2

    private var selectedMainCategory: String = ""
    private var selectedSubCategory: String = ""

    private var mainCategories = mutableListOf<String>("선택해주세요!", "강아지", "고양이", "그 외")
    private val dogBreeds = listOf("선택해주세요!", "그레이하운드", "닥스훈트", "도베르만", "러셀테리어",
    "리트리버", "말티즈", "맬러뮤트", "미니어처핀셔", "베들링턴", "불독", "비숑프리제", "사모예드", "셰퍼드",
    "슈나우저", "시바이누", "시추", "스피츠", "요크셔테리어", "웰시코기", "파피용", "퍼그", "포메라니안", "푸들",
    "허스키", "그 외") + "다시 선택"
    private val catBreeds = listOf("선택해주세요!", "노르웨이숲", "러시안블루", "랙돌", "메인쿤", "먼치킨",
        "뱅갈", "브리티쉬숏헤어", "샤미즈", "스코티시폴드", "스핑크스", "아메리칸숏헤어", "아비시니안", "코리안숏헤어",
        "터키쉬앙고라", "페르시안", "그 외" ) + "다시 선택"

//    private val REQUEST_IMAGE_SELECT = 1
//    private var selectedImageUri: Uri? = null
//    private val firestore = FirebaseFirestore.getInstance()
//    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation_profile)
        bottomNavigationView.selectedItemId = R.id.profile // 초기 선택된 항목 설정


        initializeViews()
        setupAdapters()
        setupListeners()
        mainCategorySpinner = findViewById(R.id.mainCategorySpinner)
        subCategorySpinner = findViewById(R.id.subCategorySpinner)

        subCategorySpinner.visibility = View.GONE

        val mainAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mainCategories)
        mainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mainCategorySpinner.adapter = mainAdapter

        mainCategorySpinner.setSelection(0, false)

        mainCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedMainCategory = mainCategories[position]
                Log.d("SpinnerDebug", "Selected Main Category: $selectedMainCategory")
                if (selectedMainCategory == "그 외") {
                    handleOtherCategory("Main Category", position)
                    subCategorySpinner.visibility = View.GONE
                } else {
                    (view as TextView).text = ""
                    val subCategories = if (selectedMainCategory == "강아지") dogBreeds else catBreeds
                    Log.d("SpinnerDebug", "Calling setupSubCategorySpinner with categories: $subCategories")
                    setupSubCategorySpinner(subCategories)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        subCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedSubCategory = parent.getItemAtPosition(position) as String
                if (selectedSubCategory == "그 외") {
                    handleOtherCategory("Sub Category", position)
//                    showInputDialog("Sub Category", position) { userInput ->
//                        (parent.adapter as ArrayAdapter<String>).getItem(position)?.let {
//                            (parent.adapter as ArrayAdapter<String>).remove(it)
//                            (parent.adapter as ArrayAdapter<String>).insert(userInput, position)
//                            subCategorySpinner.setSelection(position)
//                        }
//                        selectedSubCategory = userInput
//                    }
                } else if (selectedSubCategory == "다시 선택") {
                    subCategorySpinner.visibility = View.GONE
                    mainCategorySpinner.performClick()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)

        petNameEditText = findViewById(R.id.profile_add_name)
        petNeuteringEditText = findViewById(R.id.profile_add_neutering)
        petGenderEditText = findViewById(R.id.profile_add_gender)
        petSignificantEditText = findViewById(R.id.profile_add_significant)
        petAgeEditText = findViewById(R.id.profile_add_age)
        petChipNumberEditText = findViewById(R.id.profile_add_chipNumber)

        // 프로필 추가 버튼 동작
        val profileRegistrationButton = findViewById<Button>(R.id.profile_add_registration)

        profileRegistrationButton.setOnClickListener {
            petProfileRegistration()
        }
    }

    private fun setupSubCategorySpinner(subCategories: List<String>) {
        val subAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subCategories)
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subCategorySpinner.adapter = subAdapter
        subCategorySpinner.visibility = View.VISIBLE
        Log.d("SpinnerDebug", "subCategorySpinner visibility set to VISIBLE")
        }

    private fun showInputDialog(title: String, position: Int, onTextEntered: (String) -> Unit) {
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
            else -> category // 기본값 반환
        }
    }

private fun handleOtherCategory(categoryType: String, position: Int) {
    showInputDialog(categoryType, position) { userInput ->
        if (categoryType == "Main Category") {
            mainCategories[position] = userInput
            selectedMainCategory = userInput
        } else if (categoryType == "Sub Category") {
            val subAdapter = subCategorySpinner.adapter as ArrayAdapter<String>
            if (subAdapter != null) {
            subAdapter.getItem(position)?.let {
                subAdapter.remove(it)
                subAdapter.insert(userInput, position)
                selectedSubCategory = userInput // Update the selected sub category
                }
                subAdapter.notifyDataSetChanged()
                subCategorySpinner.setSelection(position)
            } else {
                Log.e("SpinnerDebug", "SubCategorySpinner adapter is not properly initialized.")
            }
        }

    }
}


    // profile 등록
    private fun petProfileRegistration() {
        val petName = petNameEditText.getText().toString().trim()
        val petKind = convertCategoryToEnglish(selectedSubCategory)
        val petNeutering = petNeuteringEditText.getText().toString().trim()
        val petGender = petGenderEditText.getText().toString().trim()
        val petSignificant = petSignificantEditText.getText().toString().trim()
        val petCategory =  convertCategoryToEnglish(selectedMainCategory)
        val petAge = petAgeEditText.getText().toString().toInt()
        val petChipNumber = petChipNumberEditText.getText().toString().toLong()

        val petCreateDTO = PetCreateDTO(petName, petKind, petNeutering, petGender, petSignificant, petCategory, petAge, petChipNumber)

        apiService.petProfileRegistration(petCreateDTO, userId).enqueue(object :
            Callback<UserResponse> {
            //        apiService.petProfileRegistration(petCreateDTO, userId).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>){
                if(response.isSuccessful) {
                    Toast.makeText(this@ProfileAddActivity,"프로필 추가 완료: ${response.body()?.message}", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this@ProfileAddActivity, "요청 실패!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@ProfileAddActivity, "network error! : ${t.message}", Toast.LENGTH_SHORT).show();
            }
        })
    }
}


=------------===================코드 최적화해달라고 말함===============================================

=================================firebase 연결 + 이미지 불러오기, 변경, 제거, 저장까지===============================
class ProfileAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAddBinding
    private val REQUEST_IMAGE_SELECT = 1
    private var selectedImageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation_profile)
        bottomNavigationView.selectedItemId = R.id.profile // 초기 선택된 항목 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())

        val imageView: ImageView = findViewById(R.id.profile_add_button)
        imageView.setOnClickListener{
        if (selectedImageUri == null) {
            selectImageFromGallery()
        } else {
            showImageOptionsDiaglog()
            }
        }

        val buttonSave: Button = findViewById(R.id.profile_add_registration)
        buttonSave.setOnClickListener {
            saveProfileData()
        }
    }


private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            val imageView: ImageView = findViewById(R.id.profile_add_button)
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun showImageOptionsDiaglog() {
        val options = arrayOf("Change Image", "Remove Image")
        AlertDialog.Builder(this)
            .setTitle("Select an options")
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

    private fun saveProfileData() {
        val name: String = findViewById<EditText>(R.id.profile_add_name).text.toString()
        val gender: String = findViewById<EditText>(R.id.profile_add_gender).text.toString()
        val age: String = findViewById<EditText>(R.id.profile_add_age).text.toString()
        val kind: String = findViewById<EditText>(R.id.profile_add_kind).text.toString()
        val neutering: String = findViewById<EditText>(R.id.profile_add_neutering).text.toString()
        val chipNumber: String = findViewById<EditText>(R.id.profile_add_chipNumber).text.toString()
        val significant: String = findViewById<EditText>(R.id.profile_add_significant).text.toString()

        //이미지 빼고 다 필수처리(비어있으면 안됨)
        if(name.isNotEmpty() && gender.isNotEmpty() && age.isNotEmpty() && kind.isNotEmpty() && neutering.isNotEmpty() &&
           chipNumber.isNotEmpty() && significant.isNotEmpty())
            if(selectedImageUri != null) {
                uploadImageToStorage(selectedImageUri!!) { imageUrl ->
                    val profileData = ProfileData(
                        name = name,
                        gender = gender,
                        age = age,
                        kind = kind,
                        neutering = neutering,
                        chipNumber = chipNumber,
                        significant = significant,
                        imageUrl = imageUrl
                    )
                    uploadDataToFireStore(profileData)
                }
            } else {
                val profileData = ProfileData(
                    name = name,
                    gender = gender,
                    age = age,
                    kind = kind,
                    neutering = neutering,
                    chipNumber = chipNumber,
                    significant = significant,
                    imageUrl = null
            )
                uploadDataToFireStore(profileData)
        } else {
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToStorage(imageUri:Uri, callback: (String) -> Unit) {
        val storageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.png")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {uri ->
                    callback(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun uploadDataToFireStore(profileData: ProfileData) {
        firestore.collection("profiles").add(profileData)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile data saved successfully.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.d("ProfileAddActivity", "Failed to save profile data")
                Toast.makeText(this, "Failed to save profile data", Toast.LENGTH_SHORT).show()
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
        intent.putExtra(MainActivity.EXTRA_SELECTED_TAB, R.id.profile)
        intent.putExtra(MainActivity.EXTRA_DESTINATION, destination)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish() // ProfileAddActivity 종료
    }
    data class ProfileData(
        val name: String,
        val gender: String,
        val kind: String,
        val age: String,
        val chipNumber: String,
        val neutering: String,
        val imageUrl: String? = null,
        val significant: String
    )
=================================firebase 연결===============================














-------------------------내가 원하는 외형은 구현됨.----------------------------------------
class ProfileAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation_profile)
        bottomNavigationView.selectedItemId = R.id.profile // 초기 선택된 항목 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())

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
        intent.putExtra(MainActivity.EXTRA_SELECTED_TAB, R.id.profile)
        intent.putExtra(MainActivity.EXTRA_DESTINATION, destination)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish() // ProfileAddActivity 종료
    }

}





-------------------------내가 원하는 외형은 구현됨.----------------------------------------

































        private lateinit var binding: ActivityProfileAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.home_menu_bottom_navigation_profile)
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())


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











    private lateinit var binding: ActivityProfileAddBinding
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragmentHome = FragmentHome()
    private val fragmentReservationList = fragment_reservation_list()
    private val fragmentFollowingList = fragment_following_list()
    private val fragmentChatList = fragment_chat_list()
    private val fragmentProfile = fragment_profile()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.home_menu_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(ItemSelectedListener())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)

        val menuItem = menu?.findItem(R.id.menu_search_icon)
        return true
    }

    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val transaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.home -> transaction.replace(R.id.menu_frame_layout, fragmentHome)
                    .commitAllowingStateLoss()

                R.id.reservationList -> transaction.replace(
                    R.id.menu_frame_layout,
                    fragmentReservationList
                ).commitAllowingStateLoss()

                R.id.follwingList -> transaction.replace(
                    R.id.menu_frame_layout,
                    fragmentFollowingList
                ).commitAllowingStateLoss()

                R.id.chatList -> transaction.replace(R.id.menu_frame_layout, fragmentChatList)
                    .commitAllowingStateLoss()

                R.id.profile -> transaction.replace(R.id.menu_frame_layout, fragmentProfile)
                    .commitAllowingStateLoss()
            }

            return true
        }
    }
}
*/