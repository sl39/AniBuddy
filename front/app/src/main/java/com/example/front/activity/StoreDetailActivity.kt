package com.example.front.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.front.ApiService
import com.example.front.R
import com.example.front.ReservationActivity
import com.example.front.RetrofitClient
import com.example.front.ReviewListFragment
import com.example.front.ServiceFragment
import com.example.front.StoreInfoFragment
import com.example.front.databinding.ActivityStoreDetailBinding
import com.example.front.retrofit.RetrofitService
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreDetailBinding
    private var storeId: Int = -1 // 매장 ID
    private lateinit var heartIcon : ImageView
    private var isFollowing = false // 팔로우 상태 추가
    private var userId: Int = -1
    private lateinit var storeCategory: String
    private lateinit var apiService: ApiService
    val context = this@StoreDetailActivity

    // 이거 이유 모르겠음
    private val followedStores = mutableListOf<Int>() // 팔로우한 매장 ID 목록

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 매장 ID 가져오기
        storeId = intent.getIntExtra("STORE_ID", -1)
        Log.d("storeIDInS.D.A","storeId=$storeId")
        userId = 1
        storeCategory = "3"

        // ViewPager와 TabLayout 설정
        setupViewPager()

        //follow 이미지 보여줄 icon 설정.
        heartIcon = findViewById(R.id.followButton)

        apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)

        // 이미지 클릭 시 토글(팔로우 안되어 있으면 팔로우, 되어 있으면 취소)
        heartIcon.setOnClickListener {
            toggleFollowing(userId, storeId, storeCategory)
        }

        //초기값 확인
        checkFollowing(userId, storeId, storeCategory)


        binding.reservationButton.setOnClickListener {
            val storeInfoFragment = supportFragmentManager.findFragmentByTag("f0") as? StoreInfoFragment
            storeInfoFragment?.let {
                val phoneNumber = it.getStorePhoneNumber() // 전화번호 가져오기
                val storeName = it.getStoreName() // 매장 이름 가져오기

                val intent = Intent(this, ReservationActivity::class.java)
                intent.putExtra("storePhoneNumber", phoneNumber) // 전화번호를 Intent에 추가
                intent.putExtra("storeName", storeName) // 매장 이름을 Intent에 추가
                startActivity(intent) // ReservationActivity 시작
            } ?: run {
                Toast.makeText(this, "전화번호를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            finish()



        }

//        updateFollowButton() // 초기 UI 업데이트

        // 매장 세부 정보 가져오기
        if (storeId != -1) {
            fetchStoreDetails(storeId)
        } else {
            Toast.makeText(this, "유효하지 않은 매장 ID입니다.", Toast.LENGTH_SHORT).show()
        }

        // 팔로우 버튼 클릭 이벤트 설정
//        setupFollowButton() // 팔로우 버튼 설정
    }

    // storeDetailActivity 화면에서 팔로우 했는지 안했는지 확인.
    private fun checkFollowing(userId: Int, storeId: Int, storeCategory: String) {
        apiService.checkFollowing(userId, storeId, storeCategory).enqueue(object :
            Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    isFollowing = response.body() ?: false
                    updateHeartIcon()
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(this@StoreDetailActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 팔로우 설정되지 않은 가게의 icon 클릭 시 팔로우 했음을 팔로우Entity에 저장 & 팔로우 한 icon으로 설정
    // 팔로우 되어 있는 가게 icon 클릭 시 팔로우Entity에서 삭제, 팔로우 하지 않은 icon으로 설정
    private fun toggleFollowing(userId: Int, storeId: Int, storeCategory: String) {
        apiService.toggleFollowing(userId, storeId, storeCategory).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // 토글 후 상태 반전
                    Log.d("storeId", "storeId=$storeId")
                    Log.d("userId", "userId=$userId")
                    Log.d("storeCategory", "storeCategory=$storeCategory")
                    isFollowing = !isFollowing
                    updateHeartIcon()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@StoreDetailActivity, "Failed to toggle follow status", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // icon 설정 여부에 따라 icon 변경.
    private fun updateHeartIcon() {
        if (isFollowing) {
            heartIcon.setImageResource(R.drawable.star_filled)
        } else {
            heartIcon.setImageResource(R.drawable.star_empty)
        }
    }


    private fun setupViewPager() {
        binding.pager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "매장정보"
                1 -> "소개"
                2 -> "리뷰"
                else -> ""
            }
        }.attach()
    }
//
//    private fun setupFollowButton() { // 팔로우 버튼 설정
//        binding.followButton.setOnClickListener {
//            if (storeId != -1) { // storeId가 유효할 때만 처리
//                isFollowing = !isFollowing
//                updateFollowStatus() // 팔로우 상태로 업데이트
//            }
//        }
//        updateFollowButton() // 초기 UI 업데이트
//    }
//
//    private fun updateFollowStatus() {
//        val followButton: ImageView = binding.followButton
//        if (isFollowing) {
//            followedStores.add(storeId)
//            followButton.setImageResource(R.drawable.redheart) // 팔로우 상태 이미지
//            Toast.makeText(this, "팔로우했습니다!", Toast.LENGTH_SHORT).show()
//            sendFollowRequest(storeId) // 팔로우 요청
//        } else {
//            followedStores.remove(storeId)
//            followButton.setImageResource(R.drawable.heart) // 기본 이미지
//            Toast.makeText(this, "팔로우 취소했습니다!", Toast.LENGTH_SHORT).show()
//            sendUnfollowRequest(storeId) // 언팔로우 요청
//        }
//    }
//
//    private fun sendFollowRequest(storeId: Int) {
//        val userId = getCurrentUserId() // 현재 사용자 ID를 가져오는 메서드
//        val followRecord = FollowRecord(userId, storeId) // FollowRecord 객체 생성
//
//        lifecycleScope.launch {
//            try {
//                val response = RetrofitService.storeService.followStore(followRecord)
//                if (response.isSuccessful) {
//                    Log.d("Follow Status", "팔로우 성공")
//                } else {
//                    Log.e("Follow Error", "팔로우 실패: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("Follow Error", "오류 발생: ${e.message}", e)
//            }
//        }
//    }
//
//    private fun sendUnfollowRequest(storeId: Int) {
//        val userId = getCurrentUserId() // 현재 사용자 ID를 가져오는 메서드
//        val followRecord = FollowRecord(userId, storeId) // FollowRecord 객체 생성
//
//        lifecycleScope.launch {
//            try {
//                val response = RetrofitService.storeService.unfollowStore(followRecord)
//                if (response.isSuccessful) {
//                    Log.d("Unfollow Status", "언팔로우 성공")
//                } else {
//                    Log.e("Unfollow Error", "언팔로우 실패: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("Unfollow Error", "오류 발생: ${e.message}", e)
//            }
//        }
//    }
//
//    private fun restoreFollowingStatus() {
//        val preferences = getSharedPreferences("StorePreferences", MODE_PRIVATE)
//        isFollowing = preferences.getBoolean("isFollowing_$storeId", false)
//    }

    private fun fetchStoreDetails(storeId: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitService.storeService.getStoreInfo(storeId)
                if (response.isSuccessful) {
                    val store = response.body()
                    if (store != null) {
                        binding.storeNameTextView.text = store.storeName
                    } else {
                        Toast.makeText(this@StoreDetailActivity, "매장 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "API 호출 실패: ${response.code()} ${response.message()}")
                    Toast.makeText(this@StoreDetailActivity, "API 호출 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("API Error", "오류 발생: ${e.message}", e)
                Toast.makeText(this@StoreDetailActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun updateFollowButton() { // 좋아요 버튼 초기화
//        val followButton: ImageView = binding.followButton
//        followButton.setImageResource(if (isFollowing) R.drawable.redheart else R.drawable.heart)
//    }

    private fun getCurrentUserId(): Int {
        val preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        return preferences.getInt("userId", -1)
    }

    override fun onPause() {
        super.onPause()
        if (storeId != -1) {
            val preferences = getSharedPreferences("StorePreferences", MODE_PRIVATE)
            preferences.edit().putBoolean("isLiked_$storeId", isFollowing).apply()
        }
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StoreInfoFragment.newInstance(storeId)
                1 -> ServiceFragment.newInstance(storeId)
                2 -> ReviewListFragment.newInstance(storeId)
                else -> StoreInfoFragment.newInstance(storeId)
            }
        }
    }
}


/*

package com.example.front

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.front.databinding.ActivityStoreDetailBinding
import com.example.front.retrofit.FollowRecord
import com.example.front.retrofit.RetrofitService
import kotlinx.coroutines.launch
import com.google.android.material.tabs.TabLayoutMediator

class StoreDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreDetailBinding
    private var storeId: Int = -1 // 매장 ID
    private lateinit var heartIcon : ImageView
    private var isFollowing = false // 팔로우 상태 추가

    // 이거 이유 모르겠음
    private val followedStores = mutableListOf<Int>() // 팔로우한 매장 ID 목록

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //follow 이미지 보여줄 icon 설정.
        heartIcon = findViewById(R.id.followButton)

        // Intent로부터 매장 ID 가져오기
        storeId = intent.getIntExtra("STORE_ID", -1)

        // ViewPager와 TabLayout 설정
        setupViewPager()

        binding.reservationButton.setOnClickListener {
            val storeInfoFragment = supportFragmentManager.findFragmentByTag("f0") as? StoreInfoFragment
            storeInfoFragment?.let {
                val phoneNumber = it.getStorePhoneNumber() // 전화번호 가져오기
                val storeName = it.getStoreName() // 매장 이름 가져오기

                val intent = Intent(this, ReservationActivity::class.java)
                intent.putExtra("storePhoneNumber", phoneNumber) // 전화번호를 Intent에 추가
                intent.putExtra("storeName", storeName) // 매장 이름을 Intent에 추가
                startActivity(intent) // ReservationActivity 시작
            } ?: run {
                Toast.makeText(this, "전화번호를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        updateFollowButton() // 초기 UI 업데이트

        // 매장 세부 정보 가져오기
        if (storeId != -1) {
            fetchStoreDetails(storeId)
        } else {
            Toast.makeText(this, "유효하지 않은 매장 ID입니다.", Toast.LENGTH_SHORT).show()
        }

        // 팔로우 버튼 클릭 이벤트 설정
        setupFollowButton() // 팔로우 버튼 설정
    }

    private fun setupViewPager() {
        binding.pager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "매장정보"
                1 -> "소개"
                2 -> "리뷰"
                else -> ""
            }
        }.attach()
    }

    private fun setupFollowButton() { // 팔로우 버튼 설정
        binding.followButton.setOnClickListener {
            if (storeId != -1) { // storeId가 유효할 때만 처리
                isFollowing = !isFollowing
                updateFollowStatus() // 팔로우 상태로 업데이트
            }
        }
        updateFollowButton() // 초기 UI 업데이트
    }

    private fun updateFollowStatus() {
        val followButton: ImageView = binding.followButton
        if (isFollowing) {
            followedStores.add(storeId)
            followButton.setImageResource(R.drawable.redheart) // 팔로우 상태 이미지
            Toast.makeText(this, "팔로우했습니다!", Toast.LENGTH_SHORT).show()
            sendFollowRequest(storeId) // 팔로우 요청
        } else {
            followedStores.remove(storeId)
            followButton.setImageResource(R.drawable.heart) // 기본 이미지
            Toast.makeText(this, "팔로우 취소했습니다!", Toast.LENGTH_SHORT).show()
            sendUnfollowRequest(storeId) // 언팔로우 요청
        }
    }

    private fun sendFollowRequest(storeId: Int) {
        val userId = getCurrentUserId() // 현재 사용자 ID를 가져오는 메서드
        val followRecord = FollowRecord(userId, storeId) // FollowRecord 객체 생성

        lifecycleScope.launch {
            try {
                val response = RetrofitService.storeService.followStore(followRecord)
                if (response.isSuccessful) {
                    Log.d("Follow Status", "팔로우 성공")
                } else {
                    Log.e("Follow Error", "팔로우 실패: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Follow Error", "오류 발생: ${e.message}", e)
            }
        }
    }

    private fun sendUnfollowRequest(storeId: Int) {
        val userId = getCurrentUserId() // 현재 사용자 ID를 가져오는 메서드
        val followRecord = FollowRecord(userId, storeId) // FollowRecord 객체 생성

        lifecycleScope.launch {
            try {
                val response = RetrofitService.storeService.unfollowStore(followRecord)
                if (response.isSuccessful) {
                    Log.d("Unfollow Status", "언팔로우 성공")
                } else {
                    Log.e("Unfollow Error", "언팔로우 실패: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Unfollow Error", "오류 발생: ${e.message}", e)
            }
        }
    }

    private fun restoreFollowingStatus() {
        val preferences = getSharedPreferences("StorePreferences", MODE_PRIVATE)
        isFollowing = preferences.getBoolean("isFollowing_$storeId", false)
    }

    private fun fetchStoreDetails(storeId: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitService.storeService.getStoreInfo(storeId)
                if (response.isSuccessful) {
                    val store = response.body()
                    if (store != null) {
                        binding.storeNameTextView.text = store.storeName
                    } else {
                        Toast.makeText(this@StoreDetailActivity, "매장 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "API 호출 실패: ${response.code()} ${response.message()}")
                    Toast.makeText(this@StoreDetailActivity, "API 호출 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("API Error", "오류 발생: ${e.message}", e)
                Toast.makeText(this@StoreDetailActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFollowButton() { // 좋아요 버튼 초기화
        val followButton: ImageView = binding.followButton
        followButton.setImageResource(if (isFollowing) R.drawable.redheart else R.drawable.heart)
    }

    private fun getCurrentUserId(): Int {
        val preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        return preferences.getInt("userId", -1)
    }

    override fun onPause() {
        super.onPause()
        if (storeId != -1) {
            val preferences = getSharedPreferences("StorePreferences", MODE_PRIVATE)
            preferences.edit().putBoolean("isLiked_$storeId", isFollowing).apply()
        }
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StoreInfoFragment.newInstance(storeId)
                1 -> ServiceFragment.newInstance(storeId)
                2 -> ReviewListFragment.newInstance(storeId)
                else -> StoreInfoFragment.newInstance(storeId)
            }
        }
    }
}

 */


    /* private lateinit var binding: ActivityStoreDetailBinding
    private var storeId: Int = -1 // 매장 ID
    private val followedStores = mutableListOf<Int>() // 팔로우한 매장 ID 목록
    private var isFollowing = false // 팔로우 상태 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 매장 ID 가져오기
        storeId = intent.getIntExtra("STORE_ID", -1)

        // ViewPager와 TabLayout 설정
        setupViewPager()

        binding.reservationButton.setOnClickListener {
            val storeInfoFragment = supportFragmentManager.findFragmentByTag("f0") as? StoreInfoFragment
            storeInfoFragment?.let {
                val phoneNumber = it.getStorePhoneNumber() // 전화번호 가져오기
                val storeName = it.getStoreName() // 매장 이름 가져오기

                val intent = Intent(this, ReservationActivity::class.java)
                intent.putExtra("storePhoneNumber", phoneNumber) // 전화번호를 Intent에 추가
                intent.putExtra("storeName", storeName) // 매장 이름을 Intent에 추가
                startActivity(intent) // ReservationActivity 시작
            } ?: run {
                Toast.makeText(this, "전화번호를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        updateFollowButton() // 초기 UI 업데이트

        // 매장 세부 정보 가져오기
        if (storeId != -1) {
            fetchStoreDetails(storeId,this)
        } else {
            Toast.makeText(this, "유효하지 않은 매장 ID입니다.", Toast.LENGTH_SHORT).show()
        }

        // 팔로우 버튼 클릭 이벤트 설정
        setupFollowButton() // 팔로우 버튼 설정
    }

    private fun setupViewPager() {
        binding.pager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "매장정보"
                1 -> "소개"
                2 -> "리뷰"
                else -> ""
            }
        }.attach()
    }

    private fun setupFollowButton() { // 팔로우 버튼 설정
        binding.followButton.setOnClickListener {
            if (storeId != -1) { // storeId가 유효할 때만 처리
                isFollowing = !isFollowing
                updateFollowStatus() // 팔로우 상태로 업데이트
            }
        }
        updateFollowButton() // 초기 UI 업데이트
    }

    private fun updateFollowStatus() {
        val followButton: ImageView = binding.followButton
        if (isFollowing) {
            followedStores.add(storeId)
            followButton.setImageResource(R.drawable.redheart) // 팔로우 상태 이미지
            Toast.makeText(this, "팔로우했습니다!", Toast.LENGTH_SHORT).show()
            sendFollowRequest(storeId) // 팔로우 요청
        } else {
            followedStores.remove(storeId)
            followButton.setImageResource(R.drawable.heart) // 기본 이미지
            Toast.makeText(this, "팔로우 취소했습니다!", Toast.LENGTH_SHORT).show()
            sendUnfollowRequest(storeId) // 언팔로우 요청
        }
    }

    private fun sendFollowRequest(storeId: Int) {
        val userId = getCurrentUserId() // 현재 사용자 ID를 가져오는 메서드
        val followRecord = FollowRecord(userId, storeId) // FollowRecord 객체 생성

        lifecycleScope.launch {
            try {
                val response = RetrofitService.storeService.followStore(followRecord)
                if (response.isSuccessful) {
                    Log.d("Follow Status", "팔로우 성공")
                } else {
                    Log.e("Follow Error", "팔로우 실패: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Follow Error", "오류 발생: ${e.message}", e)
            }
        }
    }

    private fun sendUnfollowRequest(storeId: Int) {
        val userId = getCurrentUserId() // 현재 사용자 ID를 가져오는 메서드
        val followRecord = FollowRecord(userId, storeId) // FollowRecord 객체 생성

        lifecycleScope.launch {
            try {
                val response = RetrofitService.storeService.unfollowStore(followRecord)
                if (response.isSuccessful) {
                    Log.d("Unfollow Status", "언팔로우 성공")
                } else {
                    Log.e("Unfollow Error", "언팔로우 실패: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Unfollow Error", "오류 발생: ${e.message}", e)
            }
        }
    }

    private fun restoreFollowingStatus() {
        val preferences = getSharedPreferences("StorePreferences", MODE_PRIVATE)
        isFollowing = preferences.getBoolean("isFollowing_$storeId", false)
    }

    private fun fetchStoreDetails(storeId: Int,context: Context) {

        lifecycleScope.launch {
            try {
                RetrofitService.init(context)
                val response = RetrofitService.storeService.getStoreInfo(storeId)
                if (response.isSuccessful) {
                    val store = response.body()
                    if (store != null) {
                        binding.storeNameTextView.text = store.storeName
//                        if(store.storeImageList.isNotEmpty()){
//
//                        }
                    } else {
                        Toast.makeText(this@StoreDetailActivity, "매장 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "API 호출 실패: ${response.code()} ${response.message()}")
                    Toast.makeText(this@StoreDetailActivity, "API 호출 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("API Error", "오류 발생: ${e.message}", e)
                Toast.makeText(this@StoreDetailActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFollowButton() { // 좋아요 버튼 초기화
        val followButton: ImageView = binding.followButton
        followButton.setImageResource(if (isFollowing) R.drawable.redheart else R.drawable.heart)
    }

    private fun getCurrentUserId(): Int {
        val preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        return preferences.getInt("userId", -1)
    }

    override fun onPause() {
        super.onPause()
        if (storeId != -1) {
            val preferences = getSharedPreferences("StorePreferences", MODE_PRIVATE)
            preferences.edit().putBoolean("isLiked_$storeId", isFollowing).apply()
        }
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StoreInfoFragment.newInstance(storeId)
                1 -> ServiceFragment.newInstance(storeId)
                2 -> ReviewListFragment.newInstance(storeId)
                else -> StoreInfoFragment.newInstance(storeId)
            }
        }
    }
}


     */