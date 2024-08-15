package com.example.front

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.front.data.response.NewAddressListAreaCdSearchAll
import com.example.front.data.response.NewAddressListResponse
import com.example.front.databinding.AddressRecyclerviewBinding

class AddressRecyclerviewHolder(val binding : AddressRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

class AddressAdapter(val datas: MutableList<NewAddressListAreaCdSearchAll>, val itemClickListener : OnItemClickListener, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener{
        fun  onItemClick(review: NewAddressListAreaCdSearchAll)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        AddressRecyclerviewHolder(AddressRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as AddressRecyclerviewHolder).binding
        val data = datas[position]
        binding.address.text = data.rnAdres
        binding.roadAddress.text = data.lnmAdres
        binding.root.setOnClickListener({
            itemClickListener.onItemClick(data)
        })
    }
    fun updateData(newDatas: List<NewAddressListAreaCdSearchAll>){
        datas.clear()
        datas.addAll(newDatas)
        notifyDataSetChanged()
    }


}