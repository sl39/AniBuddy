package com.example.front

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.data.ChatApiService
import com.example.front.data.response.ChatRoomResponse
import com.example.front.databinding.FragmentChatroomListBinding
import com.example.front.databinding.ItemChatroomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRoomItem(val roomId: Int, var otherName: String, var otherProfileImageUrl: String, var lastChatText: String?, var lastChatDate: LocalDateTime)

class ChatRoomViewHolder(val binding: ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root)

class ChatRoomAdapter(val datas: List<ChatRoomItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //전체 아이템 개수
    override fun getItemCount() = datas.size

    //ViewHolder 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatRoomViewHolder(ItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //ViewHolder 객체에 데이터 바인딩
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = (holder as ChatRoomViewHolder).binding

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        binding.otherName.text = datas[position].otherName
        //binding.otherProfileImage.setImageURI(datas[position].otherProfileImageUrl.toUri())
        binding.lastChatText.text = datas[position].lastChatText
        binding.lastChatDate.text = datas[position].lastChatDate.format(formatter)

        binding.root.setOnClickListener {
            val intent = Intent(it.context, MessageListActivity::class.java)
            intent.putExtra("roomId", datas[position].roomId)
            intent.putExtra("otherName", datas[position].otherName)
            intent.putExtra("otherImageUrl", datas[position].otherProfileImageUrl)
            it.context.startActivity(intent)
        }
    }
}

class fragment_chatroom_list : Fragment() {

    lateinit var binding: FragmentChatroomListBinding
    var chatRoomResponseList: List<ChatRoomResponse> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatroomListBinding.inflate(inflater, container, false)

        val chatRoomItemList: MutableList<ChatRoomItem> = mutableListOf()
        val chatApi = ChatApiService.create()

        chatApi.getChatRoomList(1, "USER").enqueue(object: Callback<List<ChatRoomResponse>>{
            override fun onResponse(
                call: Call<List<ChatRoomResponse>>,
                response: Response<List<ChatRoomResponse>>
            ) {
                if(response.isSuccessful) {
                    Log.d("getChatRoomList()", "onResponse")

                    val responseList = response.body()!!
                    Log.d("list count", "list 개수 : ${responseList.size}")
                    setChatRoomList(responseList)
                }
            }
            override fun onFailure(call: Call<List<ChatRoomResponse>>, t: Throwable) {
                Log.d("getChatRoomList()", "onFailure")
            }
        })

        for(i in chatRoomResponseList){
            val chatRoomItem = ChatRoomItem(
                roomId = i.roomId,
                otherName = i.otherName,
                otherProfileImageUrl = i.otherProfileImageUrl,
                lastChatText = "last chat!!!",
                lastChatDate = LocalDateTime.now()
            )
            chatRoomItemList.add(chatRoomItem)
        }

        binding.recyclerview.adapter = ChatRoomAdapter(chatRoomItemList)
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    private fun setChatRoomList(chatRoomList: List<ChatRoomResponse>) {
        this.chatRoomResponseList = chatRoomList
    }
}
