package com.example.front

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.common.HttpWebSocket
import com.example.front.data.ChatMessage
import com.example.front.databinding.ActivityMessageListBinding
import com.example.front.databinding.ItemMyChatBinding
import com.example.front.databinding.ItemOtherChatBinding
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import okhttp3.OkHttpClient
import okhttp3.Request
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

    @RequiresApi(Build.VERSION_CODES.O)
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

//TODO: Activity로 전환될때 RoomId, 본인 Role 필요
class MessageListActivity : AppCompatActivity() {

    lateinit var binding : ActivityMessageListBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val roomId = intent.getStringExtra("roomId")
        val otherName = intent.getStringExtra("otherName")
        val otherImageUrl = intent.getStringExtra("otherImageUrl")

        supportActionBar?.title = otherName

        //todo: 1. Realm에서 채팅 내역 정보를 가져온다.
        //todo: 2. .map이라든가 써서 myMessageItem/otherMessageItem로 변환한다.

        val messageList = mutableListOf<MessageItem>()
        val db = Realm.getDefaultInstance()
        val chatMessage: RealmResults<ChatMessage> = db.where<ChatMessage>()
            .equalTo("roomId", roomId)
            .sort("createdAt")
            .findAll()

        chatMessage.forEach{ chatMsg ->
            val messageItem = MessageItem(
                name = chatMsg.senderName,
                role = chatMsg.senderRole,
                content = chatMsg.message,
                createdAt = LocalDateTime.parse(chatMsg.createdAt)
            )
            messageList.add(messageItem)
        }

        binding.recyclerview.adapter = MessageItemAdapter(messageList, "USER") //TODO: my role
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun sendWebSocket(roomId:Int, message:String) {
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder().url("ws://localhost:8080/chat/${roomId}").build()
            val websocketListener = HttpWebSocket()
            client.newWebSocket(request, websocketListener).send(message)
        } catch (e: Exception) {
            Log.e("CHATTING", "WebSocket 통신 오류 : $e")
        }
    }
}
