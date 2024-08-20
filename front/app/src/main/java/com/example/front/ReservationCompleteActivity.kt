package com.example.front

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.front.activity.MainActivity
import com.example.front.retrofit.RetrofitService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

class ReservationCompleteActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_complete)
        val resvationId = intent.getIntExtra("resvationId", -1)
        var reservationDate : String? = null
        var storeName : String = "매장 이름 없음"
        var storeLocation : String = "매장 위치 없음"
        var info : String = "특이사항 없음"
        val api = RetrofitService.reservationService(this)
        lifecycleScope.launch {
            try{
                val response = api.getReservations(resvationId)
                if (response.code() == 200){
                    val data = response.body()
                    if(data != null){
                        reservationDate = data.reservationDateTime
                        storeName = data.storeName
                        storeLocation = data.storeLocation
                        info = data.info
                        // 예약 시간 TextView 설정
                        val reservationTimeTextView = findViewById<TextView>(R.id.reservationTimeTextView)
                        reservationTimeTextView.text = "예약시간: ${reservationDate.toString()}"

                        // 가져온 데이터 설정
                        findViewById<TextView>(R.id.storeNameTextView).text = storeName
                        findViewById<TextView>(R.id.storeLocationTextView).text = storeLocation
                        findViewById<TextView>(R.id.reservationInfo).text = info

                    }
                } else {
                    Log.d("ㅋㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ", response.code().toString())
                }

            } catch(e: Exception){
                Log.d("통신안됨",e.message.toString())
            }
        }

        // Intent로부터 예약 정보 가져오기







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
