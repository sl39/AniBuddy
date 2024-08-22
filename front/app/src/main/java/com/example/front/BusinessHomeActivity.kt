package com.example.front

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class BusinessHomeActivity : AppCompatActivity() {
    private lateinit var monthYearText: TextView // 년월 텍스트뷰
    private lateinit var selectedDate: LocalDate // 년월 변수
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buisness_home)

        // 초기화
        monthYearText = findViewById(R.id.monthYearText)
        recyclerView = findViewById(R.id.recyclerView)

        // 현재 날짜 가져오기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = getCurrentDate() as LocalDate
        } else {
        }

        // 화면 설정
        setMonthView()
    }

    private fun getCurrentDate(): Any {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            Calendar.getInstance()
        }
    }


    // 날짜 형식 설정
    private fun monthYearFromDate(date: Any): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("MM월 yyyy")
            (date as LocalDate).format(formatter)
        } else {
            val calendar = date as Calendar
            val formatter = java.text.SimpleDateFormat("MM월 yyyy", Locale.getDefault())
            formatter.format(calendar.time)
        }
    }

    // 화면 설정
    private fun setMonthView() {
        // 년월 텍스트뷰 세팅
        monthYearText.text = monthYearFromDate(selectedDate)
        val dayList = daysInMonthArray(selectedDate)
        val adapter = CalendarAdapter(dayList)

        // 레이아웃 설정(7일)
        val manager = GridLayoutManager(applicationContext, 7)

        // 레이아웃 적용
        recyclerView.layoutManager = manager

        // 어댑터 적용
        recyclerView.adapter = adapter
    }


    private fun daysInMonthArray(date: Any): ArrayList<String> {
        val dayList = ArrayList<String>()
        val lastDay: Int

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yearMonth = java.time.YearMonth.from(date as LocalDate)
            lastDay = yearMonth.lengthOfMonth()
        } else {
            val calendar = date as Calendar
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        // 해당 월 첫날
        val firstDay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (date as LocalDate).withDayOfMonth(1)
        } else {
            (date as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        }

        // 첫날 요일 가져오기(월 1, 일 7)
        val dayOfWeek = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (firstDay as LocalDate).dayOfWeek.value
        } else {
            (firstDay as Calendar).get(Calendar.DAY_OF_WEEK) - 1 // Calendar의 일요일은 1부터 시작하므로 -1
        }

        for (i in 1..42) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add("")
            } else {
                dayList.add((i - dayOfWeek).toString())
            }
        }

        return dayList
    }
}

//
//    private lateinit var monthYearText: TextView
//    private lateinit var selectedDate: LocalDate
//    private lateinit var recyclerView: RecyclerView
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_buisness_home)
//
//        monthYearText = findViewById(R.id.monthYearText)
//        recyclerView = findViewById(R.id.recyclerView)
//
//        // 현재 날짜 가져오기
//        selectedDate = getCurrentDate()
//
//        setMonthView()
//
//    }
//
//
//
//    private fun getCurrentDate(): Any {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            LocalDate.now()
//        } else {
//            Calendar.getInstance()
//        }
//    }
//
//    private fun monthYearFromDate(date: LocalDate): String {
//        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DateTimeFormatter.ofPattern("MM월 yyyy")
//            (date as LocalDate).format(formatter)
//        } else {
//            val calendar = date as Calendar
//            val formatter = java.text.SimpleDateFormat("MM월 yyyy", Locale.getDefault())
//            formatter.format(calendar.time)
//        }
//        return date.format(formatter)
//    }
//
//    private fun setMonthView() {
//        monthYearText.text = monthYearFromDate(selectedDate)
//        val dayList = daysInMonthArray(selectedDate)
//        val adapter = CalendarAdapter(dayList, eventMap)
//
//        val manager = GridLayoutManager(applicationContext, 7)
//        recyclerView.layoutManager = manager
//        recyclerView.adapter = adapter
//    }
//
//    private fun daysInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
//        val dayList = ArrayList<LocalDate?>()
//        val yearMonth = YearMonth.from(date)
//        val lastDay = yearMonth.lengthOfMonth()
//
//        val firstDay = selectedDate.withDayOfMonth(1)
//        val dayOfWeek = firstDay.dayOfWeek.value
//
//        for (i in 1..42) {
//            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
//                dayList.add(null)
//            } else {
//                dayList.add(firstDay.plusDays((i - dayOfWeek).toLong()))
//            }
//        }
//
//        return dayList
//    }
//}

/*
    private lateinit var monthYearText: TextView // 년월 텍스트뷰
    private lateinit var selectedDate: LocalDate // 년월 변수
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buisness_home)

        // 초기화
        monthYearText = findViewById(R.id.monthYearText)
        val preBtn: ImageButton = findViewById(R.id.pre_btn)
        val nextBtn: ImageButton = findViewById(R.id.next_btn)
        recyclerView = findViewById(R.id.recyclerView)

        // 현재 날짜
        selectedDate = LocalDate.now()

        // 화면 설정
        setMonthView()

        // 이전달 버튼 누르면 이전달로
        preBtn.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        // 다음달 버튼 누르면 다음달로
        nextBtn.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }
    }

    // 날짜 형식 설정
    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MM월 yyyy")
        return date.format(formatter)
    }

    // 화면 설정
    private fun setMonthView() {
        // 년월 텍스트뷰 세팅
        monthYearText.text = monthYearFromDate(selectedDate)
        val dayList = daysInMonthArray(selectedDate)
        val adapter = CalendarAdapter(dayList)

        // 레이아웃 설정(7일)
        val manager = GridLayoutManager(applicationContext, 7)

        // 레이아웃 적용
        recyclerView.layoutManager = manager

        // 어댑터 적용
        recyclerView.adapter = adapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val dayList = ArrayList<String>()

        val yearMonth = YearMonth.from(date)

        // 해당 월 막날
        val lastDay = yearMonth.lengthOfMonth()

        // 해당 월 첫날
        val firstDay = selectedDate.withDayOfMonth(1)

        // 첫날 요일 가져오기(월 1, 일 7)
        val dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add("")
            } else {
                dayList.add((i - dayOfWeek).toString())
            }
        }

        return dayList
    }
}

}

 */