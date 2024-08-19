package com.example.front

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.front.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.Locale

class ReservationCompleteActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_complete)

        // Intent로부터 예약 정보 가져오기
        val selectedYear = intent.getIntExtra("selectedYear", 0)
        val selectedMonth = intent.getIntExtra("selectedMonth", 0)
        val selectedDay = intent.getIntExtra("selectedDay", 0)
        val selectedHour = intent.getIntExtra("selectedHour", 0)
        val selectedMinute = intent.getIntExtra("selectedMinute", 0)

        val storeName = intent.getStringExtra("storeName")
        val storeLocation = intent.getStringExtra("storeLocation")

        // 예약 시간 TextView 설정
        val reservationTimeTextView = findViewById<TextView>(R.id.reservationTimeTextView)
        reservationTimeTextView.text = "예약시간: $selectedYear-${selectedMonth + 1}-$selectedDay $selectedHour:$selectedMinute"

        // 이미지 버튼 설정
        val imageButton = findViewById<ImageButton>(R.id.imageButton)
        val selectedImageResId = intent.getIntExtra("selectedImageResId", R.drawable.anibuddy_logo)
        imageButton.setImageResource(selectedImageResId)

        val storeNameTextView = findViewById<TextView>(R.id.storeNameTextView)
        val storeLocationTextView = findViewById<TextView>(R.id.storeLocationTextView)

        // 가져온 데이터 설정
        findViewById<TextView>(R.id.storeNameTextView).text = storeName ?: "매장 이름 없음"
        findViewById<TextView>(R.id.storeLocationTextView).text = storeLocation ?: "매장 위치 없음"

        // 버튼 클릭 리스너 설정
        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.editReservationButton).setOnClickListener {
            startActivity(Intent(this, ReservationActivity::class.java))
        }

        findViewById<Button>(R.id.cancelReservationButton).setOnClickListener {
            // 예약 취소 확인 다이얼로그 생성
            val builder = AlertDialog.Builder(this)
            builder.setTitle("예약 취소")
            builder.setMessage("예약을 취소하시겠습니까?")
            builder.setPositiveButton("예") { dialog, which ->
                Toast.makeText(this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            builder.setNegativeButton("아니요") { dialog, which -> dialog.dismiss() }
            builder.create().show()
        }
    }
}
