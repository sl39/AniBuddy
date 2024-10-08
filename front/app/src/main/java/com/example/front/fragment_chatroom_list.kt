package com.example.front

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.activity.MessageListActivity
import com.example.front.data.ChatApiService
import com.example.front.data.ChatMessage
import com.example.front.data.Role
import com.example.front.data.response.ChatRoomResponse
import com.example.front.databinding.FragmentChatroomListBinding
import com.example.front.databinding.ItemChatroomBinding
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRoomItem(
    val roomId: Int,
    var myName: String,
    var myId: Int,
    var myRole: Role,
    var myImageUrl: String,
    var otherName: String,
    var otherImageUrl: String,
    var otherId: Int,
    var otherRole: Role,
    var lastChatText: String?,
    var lastChatDate: LocalDateTime?
)

class ChatRoomViewHolder(val binding: ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root)

class ChatRoomAdapter(val datas: List<ChatRoomItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("CRList", "onCreateViewHolder")

        return ChatRoomViewHolder(ItemChatroomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = (holder as ChatRoomViewHolder).binding

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        binding.otherName.text = datas[position].otherName
        //binding.otherProfileImage.setImageURI(datas[position].otherProfileImageUrl.toUri())
        binding.lastChatText.text = datas[position].lastChatText

        val dataText: String? = datas[position].lastChatDate?.format(formatter)
        binding.lastChatDate.text = dataText

        binding.root.setOnClickListener {
            val intent = Intent(it.context, MessageListActivity::class.java)
            intent.putExtra("roomId", datas[position].roomId)

            intent.putExtra("myId", datas[position].myId)
            intent.putExtra("myRole", datas[position].myRole.name)
            intent.putExtra("myName", datas[position].myName)
            intent.putExtra("myImageUrl", datas[position].myImageUrl)

            intent.putExtra("otherId", datas[position].otherId)
            intent.putExtra("otherRole", datas[position].otherRole.name)
            intent.putExtra("otherName", datas[position].otherName)
            intent.putExtra("otherImageUrl", datas[position].otherImageUrl)

            it.context.startActivity(intent)
        }

        Log.d("CRList", "onBindViewHolder")
    }
}

class fragment_chatroom_list : Fragment() {

    lateinit var binding: FragmentChatroomListBinding
    var chatRoomResponseList: List<ChatRoomResponse> = mutableListOf()
    val chatRoomItemList: MutableList<ChatRoomItem> = mutableListOf()
    val TAG = "chatroomList"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CRList", "onCreateView")

        binding = FragmentChatroomListBinding.inflate(inflater, container, false)
        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(context, LinearLayout.VERTICAL)
        )

        val chatApi = ChatApiService.create(requireContext())

        chatApi.getChatRoomList().enqueue(object: Callback<List<ChatRoomResponse>>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<ChatRoomResponse>>,
                response: Response<List<ChatRoomResponse>>
            ) {
                if(response.isSuccessful) {
                    val responseList = response.body()!!
                    setChatRoomList(responseList)
                }
            }
            override fun onFailure(call: Call<List<ChatRoomResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure - chatApi.getChatRoomList")
            }
        })
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setChatRoomList(chatRoomList: List<ChatRoomResponse>) {
        this.chatRoomResponseList = chatRoomList
        chatRoomItemList.clear()

        val db = Realm.getDefaultInstance()
        for(i in chatRoomResponseList){

            // 채팅방 목록에 넣을 lastDate, lastText 추출
            val chatMessage: RealmResults<ChatMessage> = db.where<ChatMessage>()
                .equalTo("roomId", i.roomId)
                .sort("createdAt")
                .findAll()

            // 채팅방에 채팅 내역이 없는 경우
            var lastChatDate: LocalDateTime? = null
            var lastChatText = ""

            if(chatMessage!!.size > 0){
                lastChatDate = LocalDateTime.parse(chatMessage!!.last()!!.createdAt)
                lastChatText = chatMessage!!.last()!!.message
            }

            val chatRoomItem = ChatRoomItem(
                roomId = i.roomId,
                myName = i.myName,
                myId = i.myId,
                myRole = Role.valueOf(i.myRole),
                myImageUrl = i.myImageUrl,
                otherName = i.otherName,
                otherImageUrl = i.otherImageUrl,
                otherId = i.otherId,
                otherRole = Role.valueOf(i.otherRole),
                lastChatText = lastChatText,
                lastChatDate = lastChatDate
            )
            chatRoomItemList.add(chatRoomItem)
        }
        db.close()

        val sortedList = chatRoomItemList.sortedByDescending {
            it.lastChatDate ?: LocalDateTime.MIN
        }

        binding.recyclerview.adapter = ChatRoomAdapter(sortedList)
    }
}
