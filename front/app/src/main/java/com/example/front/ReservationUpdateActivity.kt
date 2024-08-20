package com.example.front

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
import com.example.front.retrofit.ReservationUpdateRequest
import com.example.front.retrofit.RetrofitService
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class ReservationUpdateActivity : AppCompatActivity() {

    private lateinit var dateTimePickerButton: Button
    private lateinit var reserveButton: Button
    private lateinit var callButton: Button
//    private lateinit var imageButton: ImageButton
    private lateinit var storeNameText: TextView // TextView 추가

    private lateinit var storeAddress: String
    private lateinit var storeInfo: TextView
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0
    private var selectedHour = 0
    private var selectedMinute = 0
    private var storePhoneNumber = ""
    private var storeId: Int = -1 // 매장 ID 추가
    private var storeName: String = ""
    private var reservationDateTime : String = ""
    private var reservationId : Int = -1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)
        // UI 요소 초기화
        dateTimePickerButton = findViewById(R.id.dateTimePickerButton)
        reserveButton = findViewById(R.id.reserveButton)
        callButton = findViewById(R.id.callButton)
        storeInfo = findViewById(R.id.reservationInfo)
        storeNameText = findViewById(R.id.storeNameText) // TextView 초기화
//        imageButton = findViewById(R.id.image)


        reservationId = intent.getIntExtra("reservationId",-1)
        // 매장 관련 정보 가져오기
        var reservationDate : String? = null
        var storeLocation : String = "매장 위치 없음"
        var info : String = "특이사항 없음"
        if(reservationId != -1){
            val api = RetrofitService.reservationService(this)

            lifecycleScope.launch {
                try{
                    val response = api.getReservations(reservationId)
                    if (response.code() == 200){
                        val data = response.body()
                        if(data != null){
                            reservationDateTime = data.reservationDateTime
                            storeName = data.storeName
                            storeId = data.storeId

                            storeLocation = data.storeLocation
                            info = data.info

                            storePhoneNumber = data.storePhoneNumber
                            storeInfo.setText(info)
                            // 예약 시간 TextView 설정
                            var date = LocalDateTime.parse(reservationDateTime)
                            var dateformate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM"))
                            // 가져온 데이터 설정
                            storeNameText.text = storeName
                            dateTimePickerButton.text = dateformate


                        }
                    }

                } catch(e: Exception){
                    Log.d("통신안됨",e.message.toString())
                }
            }
        }



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
        if ((selectedYear != 0 && selectedMonth != 0 && selectedDay != 0 && selectedHour != 0 && selectedMinute != 0) || !dateTimePickerButton.text.equals("날짜와 시간 선택")  ){

            // 날짜 유효성 검사
            val currentDate = Calendar.getInstance()
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
            }

            if (selectedDate.before(currentDate) && dateTimePickerButton.text.equals("날짜와 시간 선택")) {
                Toast.makeText(this, "과거 날짜는 예약할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }

            val date = LocalDateTime.ofInstant(selectedDate.toInstant(),selectedDate.timeZone.toZoneId())
            Log.d("새로운 시간 시간",date.toString())
            val info = storeInfo.text
            var ReservationUpdateRequest : ReservationUpdateRequest
            if((selectedYear != 0 && selectedMonth != 0 && selectedDay != 0 && selectedHour != 0 && selectedMinute != 0)){
                ReservationUpdateRequest = ReservationUpdateRequest(
                    reservationId,
                    date.toString(),
                    info.toString(),
                    storeId
                )
            } else{
                ReservationUpdateRequest = ReservationUpdateRequest(
                    reservationId,
                    reservationDateTime,
                    info.toString(),
                    storeId
                )
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitService.reservationService(this@ReservationUpdateActivity).updateReservation(ReservationUpdateRequest)
                    Log.d("API Response", response.toString())

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val resvationId = handleReservationResponse(responseBody)
                            if (resvationId == -1){
                                Toast.makeText(this@ReservationUpdateActivity, "예약이 실패.", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                val completIntent = Intent(this@ReservationUpdateActivity, ReservationCompleteActivity::class.java)
                                completIntent.putExtra("resvationId",resvationId)
                                startActivity(completIntent)
                                finish() // 현재 액티비티 종료


                                // 예약 완료 페이지로 이동

                            }
                        } else {
                            Log.e("API Response Error", "Response Body is null")
                            Toast.makeText(this@ReservationUpdateActivity, "예약에 대한 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("API Response Error", "Code: ${response.code()}, Message: ${response.message()}, Body: ${response.errorBody()?.string()}")
                        Toast.makeText(this@ReservationUpdateActivity, "예약 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("Reservation Error", "오류 발생: ${e.message}", e)
                    Toast.makeText(this@ReservationUpdateActivity, "오류 발생: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            showTimeSelectionAlert()
        }
    }

    private fun handleReservationResponse(response: ReservationResponse): Int {
        if (response.resvationId != null) {
            Toast.makeText(this, "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            return response.resvationId
            // 추가: 예약 완료 후의 동작 (예: 다른 액티비티로 이동)
        }
        return -1
    }

    private val REQUEST_CODE_GALLERY = 123
//
//    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            selectedImageUri = result.data?.data
//            imageButton.setImageURI(selectedImageUri)
//        }
//    }

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

//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        imagePickerLauncher.launch(intent)
//    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_GALLERY) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 권한 허가된 경우 갤러리 열기
//                openGallery()
//            } else {
//                // 권한 거부된 경우 알림 표시
//                Toast.makeText(this, "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
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