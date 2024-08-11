package com.example.front

import android.annotation.SuppressLint
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
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.kotlin.where
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
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

class MessageListActivity : AppCompatActivity() {

    lateinit var binding : ActivityMessageListBinding

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val roomId = intent.getIntExtra("roomId", 0)
        val otherName = intent.getStringExtra("otherName")
        val otherImageUrl = intent.getStringExtra("otherImageUrl")

        supportActionBar?.title = otherName

        val messageList = mutableListOf<MessageItem>()
        val adapter = MessageItemAdapter(messageList, "USER") //TODO: my role
        val db = Realm.getDefaultInstance()

        val realmListener = RealmChangeListener<RealmResults<ChatMessage>> {
            Log.d("Realm", "realm changed!")
            it.forEach{ chatMsg ->
                val messageItem = MessageItem(
                    name = chatMsg.senderName,
                    role = chatMsg.senderRole,
                    content = chatMsg.message,
                    createdAt = LocalDateTime.parse(chatMsg.createdAt)
                )
                messageList.add(messageItem)
                Log.d("Realm", "add message!!")
            }
            adapter.notifyDataSetChanged()
            binding.recyclerview.scrollToPosition(messageList.size-1)
        }

        val chatMessage: RealmResults<ChatMessage> = db.where<ChatMessage>()
            .equalTo("roomId", roomId)
            .sort("createdAt")
            .findAll()
            .apply {
                addChangeListener(realmListener)
            }

        chatMessage.forEach{ chatMsg ->
            val messageItem = MessageItem(
                name = chatMsg.senderName,
                role = chatMsg.senderRole,
                content = chatMsg.message,
                createdAt = LocalDateTime.parse(chatMsg.createdAt)
            )
            messageList.add(messageItem)
        }

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val client = OkHttpClient()
        val request: Request =  Request.Builder().url("ws://10.0.2.2:8080/chat/$roomId").build()
        val websocketListener = HttpWebSocket()
        val webSocket = client.newWebSocket(request, websocketListener)

        //TODO: input my data
        binding.sendBtn.setOnClickListener {
            val chatMessageInfo = JSONObject()
            chatMessageInfo.put("roomId", roomId.toString())
            chatMessageInfo.put("message", binding.msgEditText.text.toString())
            chatMessageInfo.put("senderRole", "USER")
            chatMessageInfo.put("senderId", "1")
            chatMessageInfo.put("senderName", "test user")
            webSocket.send(chatMessageInfo.toString())
            Log.d("Realm", "sendBtn - setOnClickListener")

            binding.msgEditText.setText("")
        }

        binding.recyclerview.scrollToPosition(messageList.size-1)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
