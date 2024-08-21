package com.example.front

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.front.activity.MainActivity
import com.example.front.retrofit.RetrofitService
import com.example.front.retrofit.UpdateReservationStateRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReservationCompleteActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_complete)
        val reservationId = intent.getIntExtra("resvationId", -1)
        var reservationDate : String? = null
        var storeName : String = "매장 이름 없음"
        var storeLocation : String = "매장 위치 없음"
        var info : String = "특이사항 없음"
        val api = RetrofitService.reservationService(this)
        lifecycleScope.launch {
            try{
                val response = api.getReservations(reservationId)
                if (response.code() == 200){
                    val data = response.body()
                    if(data != null){
                        reservationDate = data.reservationDateTime
                        storeName = data.storeName
                        storeLocation = data.storeLocation
                        info = data.info
                        // 예약 시간 TextView 설정
                        val reservationTimeTextView = findViewById<TextView>(R.id.reservationTimeTextView)
                        var date = LocalDateTime.parse(reservationDate)
                        var dateformate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM"))
                        reservationTimeTextView.text = "예약시간: ${dateformate}"

                        // 가져온 데이터 설정
                        findViewById<TextView>(R.id.storeNameTextView).text = "가게 이름: ${storeName}"
                        findViewById<TextView>(R.id.storeLocationTextView).text =  "주소: ${storeLocation}"
                        findViewById<TextView>(R.id.reservationInfo).text = "예약 세부 사항: ${info}"
                    }
                } else {
                    Log.d("ㅋㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ", response.code().toString())
                }

            } catch(e: Exception){
                Log.d("통신안됨",e.message.toString())
            }
        }



        // 버튼 클릭 리스너 설정
        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.editReservationButton).setOnClickListener {
            val intent = Intent(this, ReservationUpdateActivity::class.java)
            intent.putExtra("reservationId", reservationId)
            startActivity(intent)
        }

        findViewById<Button>(R.id.cancelReservationButton).setOnClickListener {
            // 예약 취소 확인 다이얼로그 생성
            val builder = AlertDialog.Builder(this)
            builder.setTitle("예약 취소")
            builder.setMessage("예약을 취소하시겠습니까?")
            builder.setPositiveButton("예") { dialog, which ->

                val api = RetrofitService.reservationService(this)
                lifecycleScope.launch {
                    try {
                        val updateReservationStateRequest = UpdateReservationStateRequest(reservationId,2)
                        val response = api.updateReservationState(updateReservationStateRequest)
                        if(response.code() == 200){
                            Toast.makeText(this@ReservationCompleteActivity, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@ReservationCompleteActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@ReservationCompleteActivity, "예약이 취소실패.", Toast.LENGTH_SHORT).show()
                            
                        }
                    } catch (e: Exception){
                        Toast.makeText(this@ReservationCompleteActivity, "예약이 취소 통신실패.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            builder.setNegativeButton("아니요") { dialog, which -> dialog.dismiss() }
            builder.create().show()
        }
    }
}
