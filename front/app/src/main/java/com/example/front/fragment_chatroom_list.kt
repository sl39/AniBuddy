package com.example.front

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.databinding.FragmentChatroomListBinding
import com.example.front.databinding.ItemChatroomBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRoomItem(var storeName: String, val lastChat: String?, val lastChatDate: LocalDateTime)

class ChatRoomViewHolder(val binding: ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root)

class ChatRoomAdapter(val datas: List<ChatRoomItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //전체 아이템 개수
    override fun getItemCount() = datas.size

    //ViewHolder 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatRoomViewHolder(ItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //ViewHolder 객체에 데이터 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = (holder as ChatRoomViewHolder).binding

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        binding.storeName.text = datas[position].storeName
        binding.lastChat.text = datas[position].lastChat
        binding.lastDate.text = datas[position].lastChatDate.format(formatter)

        binding.root.setOnClickListener {
            val intent = Intent(it.context, MessageListActivity::class.java)
            it.context.startActivity(intent)
        }
    }
}

class fragment_chatroom_list : Fragment() {

    lateinit var binding: FragmentChatroomListBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatroomListBinding.inflate(inflater, container, false)

        //todo: 백에서 채팅방 정보 가져와서 넣어주기
        val testList = mutableListOf<ChatRoomItem>()

        for(i in 1..10){
            val testChatRoomItem = ChatRoomItem(
                storeName = "test store $i",
                lastChat = "test chat $i",
                lastChatDate = LocalDateTime.now()
            )
            testList.add(testChatRoomItem)
        }

        binding.recyclerview.adapter = ChatRoomAdapter(testList)
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

}