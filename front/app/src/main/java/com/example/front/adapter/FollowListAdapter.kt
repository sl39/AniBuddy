package com.example.front.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.front.R

class FollowingListAdapter(private val followedStores: List<Int>) : RecyclerView.Adapter<FollowingListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeIdTextView: TextView = view.findViewById(R.id.storeNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_following_store, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.storeIdTextView.text = "Store ID: ${followedStores[position]}"
    }

    override fun getItemCount() = followedStores.size
}
