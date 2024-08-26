package com.example.front.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.front.R
import com.example.front.databinding.HomeReviewRecyclerview2Binding
import com.example.front.databinding.HomeReviewRecyclerviewBinding
import com.example.front.retrofit.ReviewObject

class ReviewAdapter(
    private val datas: MutableList<ReviewObject>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(review: ReviewObject)
    }

    private val imageUrls: MutableMap<Int, List<String>> = mutableMapOf()

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = HomeReviewRecyclerview2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = datas[position]
        val urls = imageUrls[review.storeId] ?: emptyList()
        holder.bind(review, urls)

        holder.itemView.setOnClickListener { itemClickListener.onItemClick(review) }
    }

    fun addImages(reviewId: Int, images: List<String>) {
        if (images.isNotEmpty()) {
            imageUrls[reviewId] = images
            notifyItemChanged(datas.indexOfFirst { it.reviewId == reviewId })
        }
    }

    class ReviewViewHolder(private val binding: HomeReviewRecyclerview2Binding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewObject, imageUrls: List<String>) {
            binding.homeReviewStoreText.text = review.reviewContent
            binding.homeReviewTime.text = review.reviewTime

            if (review.reviewImageList.isNotEmpty() && review.reviewImageList[0].isNotEmpty()) {
                binding.reviewImage.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(Uri.parse(review.reviewImageList[0]))
                    .placeholder(R.drawable.anibuddy_logo)
                    .error(R.drawable.anibuddy_logo)
                    .into(binding.reviewImage)
            } else {
                binding.reviewImage.visibility = View.GONE
            }
        }
    }
}