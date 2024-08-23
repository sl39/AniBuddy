package com.example.front.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.window.OnBackInvokedDispatcher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.BuildConfig
import com.example.front.common.HttpWebSocket
import com.example.front.data.ApiService
import com.example.front.data.ChatMessage
import com.example.front.data.UserPreferencesRepository
import com.example.front.data.preferencesRepository
import com.example.front.data.response.UserTesetResponse
import com.example.front.databinding.ActivityMessageListBinding
import com.example.front.databinding.ItemMyChatBinding
import com.example.front.databinding.ItemOtherChatBinding
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var webSocket:WebSocket

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userPreferencesRepository = preferencesRepository.getUserPreferencesRepository(this@MessageListActivity)

        val intent = intent
        val roomId = intent.getIntExtra("roomId", 0)
        val myName = intent.getStringExtra("myName")
        val myId = intent.getIntExtra("myId", 0)
        val myRole = intent.getStringExtra("myRole")
        val myImageUrl = intent.getStringExtra("myImageUrl")
        val otherName = intent.getStringExtra("otherName")
        val otherImageUrl = intent.getStringExtra("otherImageUrl")
        val otherId = intent.getIntExtra("otherId", 0)
        val otherRole = intent.getStringExtra("otherRole")

        supportActionBar?.title = otherName

        val messageList = mutableListOf<MessageItem>()
        val adapter = MessageItemAdapter(messageList, myRole!!)

        val realmListener = RealmChangeListener<RealmResults<ChatMessage>> {
            Log.d("Realm", "realm changed!")
            val messageItem = MessageItem(
                name = it[it.size-1]!!.senderName,
                role = it[it.size-1]!!.senderRole,
                content = it[it.size-1]!!.message,
                createdAt = LocalDateTime.parse(it[it.size-1]!!.createdAt)
            )
            messageList.add(messageItem)
            Log.d("Realm", "add message!!")
            adapter.notifyDataSetChanged()
            binding.recyclerview.scrollToPosition(messageList.size-1)
        }

        val db = Realm.getDefaultInstance()
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

        val api = ApiService.create(this)

        api.getUserTest().enqueue(object : Callback<UserTesetResponse> {
            override fun onResponse(
                call: Call<UserTesetResponse>,
                response: Response<UserTesetResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { Log.d("이게 userId 로 받아와야 됨 userName", it.role)}
                    val accessToken = runBlocking {
                        userPreferencesRepository.getAccessToken.first()
                    }

                    val client = OkHttpClient()
                    val request: Request = Request.Builder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .url("ws://3.36.140.201:8080/chat/$roomId").build()

                    val websocketListener = HttpWebSocket()
                    webSocket = client.newWebSocket(request, websocketListener)

                } else {
                    onFailure(call, Throwable("Unsuccessful response"))
                }
            }

            override fun onFailure(call: Call<UserTesetResponse>, t: Throwable) {
            }
        })

        binding.sendBtn.setOnClickListener {
            //빈칸이거나 공백밖에 없으면 전송 안되게 하기
            if(binding.msgEditText.text.toString().isNotBlank()){
                val chatMessageInfo = JSONObject()
                chatMessageInfo.put("roomId", roomId.toString())
                chatMessageInfo.put("senderName", myName)
                chatMessageInfo.put("senderId", myId.toString())
                chatMessageInfo.put("senderRole", myRole)
                chatMessageInfo.put("senderImageUrl", myImageUrl)
                chatMessageInfo.put("message", binding.msgEditText.text.toString())
                chatMessageInfo.put("receiverRole", otherRole)
                chatMessageInfo.put("receiverId", otherId.toString())
                chatMessageInfo.put("receiverName", otherName)
                chatMessageInfo.put("receiverImageUrl", otherImageUrl)

                webSocket.send(chatMessageInfo.toString())
                Log.d("Realm", "sendBtn - setOnClickListener")

                binding.msgEditText.setText("")
            }
        }

        binding.recyclerview.scrollToPosition(messageList.size-1)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket.close(1000, "ChatRoom Activity 종료로 웹소켓 소멸")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
