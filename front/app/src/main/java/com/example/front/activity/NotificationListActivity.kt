package com.example.front.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.R
import com.example.front.data.ChatApiService
import com.example.front.data.response.ChatRoomResponse
import com.example.front.data.response.NotificationResponse
import com.example.front.databinding.ActivityNotificationListBinding
import com.example.front.databinding.ItemNotificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationItem(
    var notificationTitle: String,
    var notificationContent: String,
    var notificationDate: String,
)

class NotificationViewHolder(val binding: ItemNotificationBinding): RecyclerView.ViewHolder(binding.root)

class NotificationAdapter(val datas: List<NotificationItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return NotificationViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("onBind", datas[position].notificationTitle)

        val binding = (holder as NotificationViewHolder).binding
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        binding.notificationTitle.text = datas[position].notificationTitle
        binding.notificationContent.text = datas[position].notificationContent
        binding.notificationDate.text = LocalDateTime.parse(datas[position].notificationDate).format(formatter)
    }
}

class NotificationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationListBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "알림"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.notificationRecyclerview.addItemDecoration(
            DividerItemDecoration(applicationContext, LinearLayout.VERTICAL)
        )

        binding.notificationRecyclerview.layoutManager = LinearLayoutManager(this)

        //알림 리스트 생성
        val itemList = mutableListOf<NotificationItem>()
        val chatApi = ChatApiService.create(applicationContext)

        chatApi.getNotificationList().enqueue(object: Callback<List<NotificationResponse>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<NotificationResponse>>,
                response: Response<List<NotificationResponse>>
            ) {
                if(response.isSuccessful) {
                    val responseList = response.body()!!
                    Log.d("chatApi","get NotificationList! size: ${responseList.size}")

                    itemList.clear()
                    for(i in responseList) {
                        val item = NotificationItem(
                            notificationTitle = i.title,
                            notificationContent = i.content,
                            notificationDate = i.notified_at
                        )
                        itemList.add(item)
                    }
                    val sortedList = itemList.sortedByDescending {
                        LocalDateTime.parse(it.notificationDate)
                    }
                    binding.notificationRecyclerview.adapter = NotificationAdapter(sortedList)
                    Log.d("chatApi", "size: ${ itemList.size}" )
                }
            }
            override fun onFailure(call: Call<List<NotificationResponse>>, t: Throwable) {
                Log.e("chatApi", "chatApi.getNotificationList - $t")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}