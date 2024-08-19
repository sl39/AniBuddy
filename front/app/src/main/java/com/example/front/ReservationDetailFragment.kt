package com.example.front

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReservationDetailFragment : Fragment(R.layout.fragment_reservation_detail) { // XML 레이아웃 파일 이름에 맞게 수정

    private lateinit var reservationDetailPagerAdapter: ReservationDetailPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        reservationDetailPagerAdapter = ReservationDetailPagerAdapter(this)
        viewPager.adapter = reservationDetailPagerAdapter

        // TabLayout과 ViewPager2를 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "진행예약"
                1 -> "완료예약"
                else -> null
            }
        }.attach()
    }

    private inner class ReservationDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2 // 진행 중인 예약과 완료된 예약
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FragmentReservationList() // 진행 중인 예약 프래그먼트
                1 -> FragmentReservationCompleteList() // 완료된 예약 프래그먼트
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }
}
