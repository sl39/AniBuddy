package com.example.front

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.activity.ReservationCompleteOwnerActivity
import com.example.front.adapter.ReservationAdapter
import com.example.front.adapter.ReservationAdapterOwner
import com.example.front.retrofit.Reservation
import com.example.front.retrofit.ReservationService
import com.example.front.retrofit.RetrofitService
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class fragment_reservation_ing_owner_list : Fragment() {

    private lateinit var reservationService: ReservationService
    private lateinit var reservationAdapter: ReservationAdapterOwner
    private val reservations = mutableListOf<Reservation>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        Log.d("오너 리스트 들어옵니까", "아 들어오나요!")

        // 클릭 리스너를 포함한 어댑터 초기화
        reservationAdapter = ReservationAdapterOwner(reservations) { reservation ->
            // 클릭 시 ReservationCompleteActivity로 이동
            val intent = Intent(context, ReservationCompleteOwnerActivity::class.java).apply {
                putExtra("resvationId", reservation.id) // 예약 ID 전달

            }
            startActivity(intent)
        }

        recyclerView.adapter = reservationAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // RetrofitService를 통해 reservationService 초기화
        reservationService = RetrofitService.reservationService(requireContext())

        fetchReservations()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchReservations() {
        lifecycleScope.launch {
            try {
                // 로딩 스피너 시작
                // showLoadingSpinner()

                val response = reservationService.getAllReservationsOwner()
                if (response.isSuccessful) {
                    Log.d("오너 리스트 들어옵니까", "아 들어오나요!2222222")

                    response.body()?.let { allReservations ->
                        val currentTime = Calendar.getInstance() // 현재 시간 가져오기
                        // 현재 시간보다 예약 시간이 늦은 예약만 필터링
                        reservations.clear()
                        reservations.addAll(allReservations.filter { reservation ->
                            val date = LocalDateTime.parse(reservation.reservationTime)
                            val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                            // 예약 시간이 현재 시간보다 크거나 같은지 비교
                            date > now && reservation.state < 2

                        })
                        reservationAdapter.notifyDataSetChanged()

                        // 예약이 없을 경우 메시지 표시
                        if (reservations.isEmpty()) {
                            // showEmptyMessage()
                        }
                    }
                } else {
                    Log.e("FragmentReservationList", "Error fetching reservations: ${response.errorBody()?.string()}")
                    // 에러 메시지를 사용자에게 표시
                    // Toast.makeText(context, "예약 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("FragmentReservationList", "Exception while fetching reservations", e)
                // 예외 처리
                // Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                // 로딩 스피너 종료
                // hideLoadingSpinner()
            }
        }
    }
}
