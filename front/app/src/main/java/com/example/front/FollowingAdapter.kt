package com.example.front

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FollowingAdapter(private var followings: List<StoreFollowDTO>, private val onItemClick: (StoreFollowDTO) -> Unit) : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.following_store_recyclerview, parent, false)
            return FollowingViewHolder(view)
        }

        override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
            val follow = followings[position]
            holder.bind(follow)
            holder.itemView.setOnClickListener {
                onItemClick(follow)
            }
        }

    fun setFollowList(newFollowList: List<StoreFollowDTO>) {
        this.followings = newFollowList
        notifyDataSetChanged() // 데이터 변경 알림
    }


        override fun getItemCount(): Int = followings.size

        class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val storeNameTextView: TextView =
                itemView.findViewById(R.id.followingList1Name)
            private val storeAddressTextView: TextView =
                itemView.findViewById(R.id.followingList1Address)
            private val storeCategoryTextView: TextView =
                itemView.findViewById(R.id.followingList1Cateogry)
            private val base64ImageView: ImageView =
                itemView.findViewById(R.id.followList1)


            fun bind(follow: StoreFollowDTO) {
                storeNameTextView.text = follow.storeName
                storeAddressTextView.text = follow.address
                storeCategoryTextView.text = follow.roadaddress

//                Log.d("followsize?", "followsize = ${followings.size}")
                Log.d("floowImage?", "followImage = ${follow.storeImageList}")
                
                // 이미지가 여러 개 일 때 처음 이미지만 불러오도록 설정
                val imageUrl = follow.storeImageList.firstOrNull()
                if(imageUrl != null) {
                    Glide.with(itemView.context)
                        .load(follow.storeImageList) // 프로필 이미지 URL , 절대경로,상대경로확인?
                        .placeholder(R.drawable.anibuddy_logo) // 로딩 중 표시할 이미지
                        .error(R.drawable.anibuddy_logo) // 로드 실패 시 표시할 이미지
                        .into(base64ImageView) // 이미지를 로드할 ImageView
                }
        }
    }
}