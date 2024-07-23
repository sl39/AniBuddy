package com.example.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.front.databinding.ActivityMessageListBinding
import com.example.front.databinding.ItemMyChatBinding
import com.example.front.databinding.ItemOtherChatBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MessageItem(var name: String, val role: String, val content: String, val createdAt: LocalDateTime) {}

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
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        if(getItemViewType(position) == MY_CHAT) {
            val binding = (holder as MyChatItemViewHolder).binding
            binding.chatDate.text = formatter.format(datas[position].createdAt)
            binding.chatContent.text = datas[position].content
        } else {
            val binding = (holder as OtherChatItemViewHolder).binding
            binding.otherName.text = datas[position].name
            binding.chatDate.text = formatter.format(datas[position].createdAt)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_message_list)

    }
}
