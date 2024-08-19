package com.example.front

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.retrofit.Reservation
import com.example.front.retrofit.RetrofitService
import kotlinx.coroutines.launch
import com.example.front.adapter.ReservationAdapter
import com.example.front.retrofit.ReservationService
import java.util.Calendar

class FragmentReservationList : Fragment() {

    private lateinit var reservationService: ReservationService
    private lateinit var reservationAdapter: ReservationAdapter
    private val reservations = mutableListOf<Reservation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // 클릭 리스너를 포함한 어댑터 초기화
        reservationAdapter = ReservationAdapter(reservations) { reservation ->
            // 클릭 시 ReservationCompleteActivity로 이동
            val intent = Intent(context, ReservationCompleteActivity::class.java).apply {
                putExtra("reservationId", reservation.id) // 예약 ID 전달
                putExtra("storeId", reservation.storeId) // 매장 ID 전달
                putExtra("storeName", reservation.storeName) // 매장 이름 전달
                putExtra("storeAddress", reservation.storeAddress) // 매장 주소 전달
            }
            startActivity(intent)
        }

        recyclerView.adapter = reservationAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // RetrofitService를 통해 reservationService 초기화
        reservationService = RetrofitService.reservationService

        fetchReservations()

        return view
    }

    private fun fetchReservations() {
        lifecycleScope.launch {
            try {
                // 로딩 스피너 시작
                // showLoadingSpinner()

                val response = reservationService.getReservations()
                if (response.isSuccessful) {
                    response.body()?.let { allReservations ->
                        val currentTime = Calendar.getInstance() // 현재 시간 가져오기
                        // 현재 시간보다 예약 시간이 늦은 예약만 필터링
                        reservations.clear()
                        reservations.addAll(allReservations.filter { reservation ->
                            // 예약 시간 계산
                            val reservationTime = Calendar.getInstance().apply {
                                set(reservation.year, reservation.month - 1, reservation.day, reservation.hour, reservation.minute) // 월은 0부터 시작
                            }
                            // 예약 시간이 현재 시간보다 크거나 같은지 비교
                            reservationTime.after(currentTime) || reservationTime == currentTime
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
