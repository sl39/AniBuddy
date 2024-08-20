package com.example.front

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.front.retrofit.ReservationRequest
import com.example.front.retrofit.ReservationResponse
import com.example.front.retrofit.RetrofitService
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone


class ReservationActivity : AppCompatActivity() {

    private lateinit var dateTimePickerButton: Button
    private lateinit var reserveButton: Button
    private lateinit var callButton: Button
    private lateinit var imageButton: ImageButton
    private lateinit var storeNameText: TextView // TextView 추가
    private lateinit var storeName: String
    private lateinit var storeAddress: String
    private lateinit var storeInfo: TextView
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0
    private var selectedHour = 0
    private var selectedMinute = 0
    private var selectedImageUri: Uri? = null
    private var storePhoneNumber: String? = null
    private var storeId: Int = -1 // 매장 ID 추가

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        storeNameText = findViewById(R.id.storeNameText)
        // 매장 관련 정보 가져오기
        storeName = intent.getStringExtra("storeName") ?: ""
        storePhoneNumber = intent.getStringExtra("storePhoneNumber") ?: ""
        storeAddress = intent.getStringExtra("storeAddress") ?: ""
        storeId = intent.getIntExtra("storeId", -1)


        // UI 요소 초기화
        dateTimePickerButton = findViewById(R.id.dateTimePickerButton)
        reserveButton = findViewById(R.id.reserveButton)
        callButton = findViewById(R.id.callButton)
        storeInfo = findViewById(R.id.reservationInfo)
        storeNameText = findViewById(R.id.storeNameText) // TextView 초기화
//        imageButton = findViewById(R.id.image)

        // 매장이름 설정
        storeNameText.text = storeName


        dateTimePickerButton.setOnClickListener {
            showDateTimePickerDialog()
        }

        reserveButton.setOnClickListener {
            reserveButtonClicked()
        }

        callButton.setOnClickListener {
            storePhoneNumber?.let {
                val phoneIntent = Intent(Intent.ACTION_DIAL)
                phoneIntent.data = Uri.parse("tel:$it")  // 가져온 전화번호 사용
                startActivity(phoneIntent)
            } ?: run {
                Toast.makeText(this, "전화번호를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

//        imageButton.setOnClickListener {
//            showImageSelectionDialog()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun reserveButtonClicked() {
        // 날짜 및 시간 선택 여부 확인
        if (selectedYear != 0 && selectedMonth != 0 && selectedDay != 0 && selectedHour != 0 && selectedMinute != 0) {

            // 날짜 유효성 검사
            val currentDate = Calendar.getInstance()
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
            }

            if (selectedDate.before(currentDate)) {
                Toast.makeText(this, "과거 날짜는 예약할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }

            val date = LocalDateTime.ofInstant(selectedDate.toInstant(),selectedDate.timeZone.toZoneId())
            val info = storeInfo.text.toString()

            // 예약 정보 생성
            val reservationRequest = ReservationRequest(
                date.toString(),
                info,
                storeId
            )

            Log.d("ReservationRequest", reservationRequest.toString())

            lifecycleScope.launch {
                try {
                    val response = RetrofitService.reservationService(this@ReservationActivity).createReservation(reservationRequest)
                    Log.d("API Response", response.toString())

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            handleReservationResponse(responseBody)

                            // 예약 완료 페이지로 이동
                            val completIntent = Intent(this@ReservationActivity, ReservationCompleteActivity::class.java).apply {
                                putExtra("storeName", storeName)  // 매장 이름 추가
                                putExtra("storeAddress", storeAddress)  // 매장 주소 추가
                                putExtra("storePhoneNumber", storePhoneNumber)  // 전화번호 추가
                                putExtra("selectedYear", selectedYear)
                                putExtra("selectedMonth", selectedMonth)
                                putExtra("selectedDay", selectedDay)
                                putExtra("selectedHour", selectedHour)
                                putExtra("selectedMinute", selectedMinute)
                                putExtra("reservationTime", selectedDate.timeInMillis) // 예약 시간 추가
                                putExtra("selectedImageResId", R.drawable.anibuddy_logo) // 이미지 리소스 추가
                            }
                            startActivity(completIntent)
                            finish() // 현재 액티비티 종료
                        } else {
                            Log.e("API Response Error", "Response Body is null")
                            Toast.makeText(this@ReservationActivity, "예약에 대한 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("API Response Error", "Code: ${response.code()}, Message: ${response.message()}, Body: ${response.errorBody()?.string()}")
                        Toast.makeText(this@ReservationActivity, "예약 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("Reservation Error", "오류 발생: ${e.message}", e)
                    Toast.makeText(this@ReservationActivity, "오류 발생: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            showTimeSelectionAlert()
        }
    }

    private fun handleReservationResponse(response: ReservationResponse) {
        if (response.success) {
            Toast.makeText(this, "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            // 추가: 예약 완료 후의 동작 (예: 다른 액티비티로 이동)
        } else {
            Toast.makeText(this, "예약 오류: ${response.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private val REQUEST_CODE_GALLERY = 123

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            imageButton.setImageURI(selectedImageUri)
        }
    }

//    private fun showImageSelectionDialog() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        imagePickerLauncher.launch(intent)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
//            } else {
//                openGallery()
//            }
//        } else {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
//            } else {
//                openGallery()
//            }
//        }
//    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허가된 경우 갤러리 열기
                openGallery()
            } else {
                // 권한 거부된 경우 알림 표시
                Toast.makeText(this, "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showDateTimePickerDialog() {
        showDatePickerDialog()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth
                showTimePickerDialog()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            selectedHour = hourOfDay
            selectedMinute = minute
            val selectedDateTime = "$selectedYear-${selectedMonth + 1}-$selectedDay $selectedHour:$selectedMinute"
            dateTimePickerButton.text = selectedDateTime
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    private fun showTimeSelectionAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("예약 시간 선택")
        builder.setMessage("예약 시간을 선택해주세요.")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}