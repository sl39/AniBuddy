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
import com.example.front.retrofit.RetrofitService
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class fragment_reservation_complete_owner_list : Fragment() {
    private var reservationList: List<Reservation> = listOf()
    private lateinit var reservationAdapter: ReservationAdapterOwner

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_complete_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // 클릭 리스너를 포함한 어댑터 초기화
        reservationAdapter = ReservationAdapterOwner(reservationList.toMutableList()) { reservation ->
            // 클릭 시 ReservationCompleteActivity로 이동
            val intent = Intent(context, ReservationCompleteOwnerActivity::class.java).apply {
                putExtra("resvationId", reservation.id) // 예약 ID 전달
            }
            startActivity(intent)
        }

        recyclerView.adapter = reservationAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 완료된 예약 목록을 가져오는 로직 추가
        fetchCompletedReservations()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchCompletedReservations() {
        lifecycleScope.launch {
            try {

                val response = RetrofitService.reservationService(requireContext()).getAllReservationsOwner() // 예약 목록 가져오기
                if (response.isSuccessful) {
                    response.body()?.let { allReservations ->
                        val currentTime = Calendar.getInstance() // 현재 시간 가져오기

                        // 예약일자가 지난 예약만 필터링
                        reservationList = allReservations.filter { reservation ->
                            val date = LocalDateTime.parse(reservation.reservationTime)
                            val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                            // 예약 시간이 현재 시간보다 크거나 같은지 비교
                            date < now || reservation.state == 2

                        }
                        reservationAdapter.updateReservations(reservationList) // 어댑터에 데이터 업데이트
                    } ?: Log.e("FragmentReservationCompleteList", "Response body is null")
                } else {
                    Log.e("FragmentReservationCompleteList", "Error fetching reservations: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("FragmentReservationCompleteList", "Exception while fetching reservations", e)
            }
        }
    }
}
