package com.example.front

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.activity.StoreDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class fragment_following_list : Fragment() {
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var followAdapter: FollowingAdapter
    private var followLists: List<StoreFollowDTO> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {

            apiService = RetrofitClient.getRetrofitInstance(requireContext()).create(ApiService::class.java)

            val view = inflater.inflate(R.layout.fragment_following_list, container, false)

            recyclerView = view.findViewById(R.id.following_recyclerview)

            recyclerView.layoutManager = LinearLayoutManager(context)

//            val userId: Int? = arguments?.getInt("userId")
//
//            Log.d("FollowingFragment", "Received userId: $userId")

//            val userId = arguments?.getInt("userId")

            followAdapter = FollowingAdapter(followLists) { follow ->
//                Log.d("STORE_ID?", "STORE_ID = ${follow.id}")
//                Log.d("userId?", "userId = ${userId}")
                val intent = Intent(context, StoreDetailActivity::class.java).apply {
                putExtra("STORE_ID", follow.id)
//                putExtra("userId", userId)
            }
            startActivity(intent)
        }

        recyclerView.adapter = followAdapter
        return view
        }

    override fun onResume() {
        super.onResume()
//        val userId = arguments?.getInt("userId")
//        Log.d("찜리스트", "userId from arguments: $userId")
//        if (userId != null) {
            loadFollowList(-1)
//        }
    }

    private fun loadFollowList(userId: Int) {
        Log.d("ListFragment", "loadList 메서드 시작");
        apiService.getFollowingStoreList(userId).enqueue(object : Callback<List<StoreFollowDTO>> {
            override fun onResponse(call: Call<List<StoreFollowDTO>>, response: Response<List<StoreFollowDTO>>) {
                Log.d("ListFragment", "네트워크 요청 보냄");
                if (response.isSuccessful) {
                    Log.d("FollowingListFragment", "수신된 FollowList: $followLists")
                        followLists = response.body() ?: emptyList()
                        followAdapter.setFollowList(followLists)

                    followAdapter = FollowingAdapter(followLists) { followLists ->
                        val intent = Intent(context, StoreDetailActivity::class.java).apply {
                            putExtra("STORE_ID", followLists.id)
<<<<<<< HEAD
<<<<<<< HEAD
                            putExtra("category", followLists.storeCategory)
                            Log.d("STORE_Id","STORE_Id= ${followLists.id}")
=======
=======
>>>>>>> c381439c4f316cadcbcd3ffd84c3b245b1b0d5dd
                            putExtra("category",followLists.storeCategory)
                            Log.d("storeId","storeId= $id")
>>>>>>> c381439c4f316cadcbcd3ffd84c3b245b1b0d5dd
                        }
                        startActivity(intent)
                    }
                    recyclerView.adapter = followAdapter
                } else {
                    Log.e(
                        "FollowingFragment",
                        "응답 없음: ${response.code()} - ${response.message()}"
                    )
                    Log.e("FollowingFragment", "Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<StoreFollowDTO>>, t: Throwable) {
                showToast("네트워크 에러 발생!")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}