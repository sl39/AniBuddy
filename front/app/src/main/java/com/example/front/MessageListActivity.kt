package com.example.front

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.databinding.ActivityMessageListBinding
import com.example.front.databinding.ItemMyChatBinding
import com.example.front.databinding.ItemOtherChatBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessageItem(var name: String, val role: String, val content: String, val createdAt: LocalDateTime)

class MyChatItemViewHolder(val binding: ItemMyChatBinding) : RecyclerView.ViewHolder(binding.root)

class OtherChatItemViewHolder(val binding: ItemOtherChatBinding) : RecyclerView.ViewHolder(binding.root)

class MessageItemAdapter(val datas: List<MessageItem>, val role: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == MY_CHAT) {
            MyChatItemViewHolder(ItemMyChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            OtherChatItemViewHolder(ItemOtherChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        if(getItemViewType(position) == MY_CHAT) {
            val binding = (holder as MyChatItemViewHolder).binding
            binding.chatDate.text = datas[position].createdAt.format(formatter)
            binding.chatContent.text = datas[position].content
        } else {
            val binding = (holder as OtherChatItemViewHolder).binding
            binding.otherName.text = datas[position].name
            binding.chatDate.text = datas[position].createdAt.format(formatter)
            binding.chatContent.text = datas[position].content
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(datas[position].role == this.role) MY_CHAT
        else OTHER_CHAT
    }

    companion object {
        private const val MY_CHAT = 1
        private const val OTHER_CHAT = 2
    }
}

class MessageListActivity : AppCompatActivity() {

    lateinit var binding : ActivityMessageListBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Store Name"

        //todo: 백에서 채팅 내역 정보 가져와서 넣어주기
        val testList = mutableListOf<MessageItem>()

        for(i in 1..10){
            val myMessageItem = MessageItem(
                name = "user",
                role = "USER",
                content = "test chat $i",
                createdAt = LocalDateTime.now()
            )
            val otherMessageItem = MessageItem(
                name = "owner",
                role = "OWNER",
                content = "test chat $i",
                createdAt = LocalDateTime.now()
            )
            testList.add(myMessageItem)
            testList.add(otherMessageItem)
        }

        val myMessageItem = MessageItem(
            name = "user",
            role = "USER",
            content = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            createdAt = LocalDateTime.now()
        )
        val otherMessageItem = MessageItem(
            name = "owner",
            role = "OWNER",
            content = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
            createdAt = LocalDateTime.now()
        )
        testList.add(myMessageItem)
        testList.add(otherMessageItem)

        binding.recyclerview.adapter = MessageItemAdapter(testList, "USER")
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
    }
}
