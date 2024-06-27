package com.example.front

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.databinding.FragmentChatroomListBinding
import com.example.front.databinding.ItemChatroomBinding


class ChatRoomViewHolder(val binding: ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root)

class ChatRoomAdapter(val datas: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = datas.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatRoomViewHolder(
            ItemChatroomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ChatRoomViewHolder).binding
        binding.itemStoreName.text = datas[position]
        binding.itemLatestChat.text = datas[position]
    }
}

class fragment_chatroom_list : Fragment() {

    lateinit var binding: FragmentChatroomListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatroomListBinding.inflate(inflater, container, false)

        val datas = mutableListOf<String>()

        for(i in 1..10) datas.add("store $i")

        val adapter = ChatRoomAdapter(datas)
        val layoutManager = LinearLayoutManager(activity)

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = layoutManager

        return binding.root //화면에 출력
    }
}