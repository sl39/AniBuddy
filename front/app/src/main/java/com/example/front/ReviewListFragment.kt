package com.example.front

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.front.adapter.ReviewAdapter
import com.example.front.databinding.FragmentReviewListBinding
import com.example.front.retrofit.RetrofitService
import com.example.front.retrofit.ReviewApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.front.retrofit.ReviewObject

class ReviewListFragment : Fragment(), ReviewAdapter.OnItemClickListener {
    private var storeId: Int? = null
    private val reviews: MutableList<ReviewObject> = mutableListOf()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewApi: ReviewApi


    companion object {
        fun newInstance(storeId: Int): ReviewListFragment {
            val fragment = ReviewListFragment()
            val args = Bundle()
            args.putInt("STORE_ID", storeId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt("STORE_ID")
        }
        reviewApi = ReviewApi.create(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentReviewListBinding.inflate(inflater, container, false)

        reviewAdapter = ReviewAdapter(reviews, this)
        binding.reviewRecyclerView.adapter = reviewAdapter
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadReviews()

        return binding.root
    }

    override fun onItemClick(review: ReviewObject) {
        Toast.makeText(requireContext(), "리뷰 ID: ${review.reviewId} 클릭됨", Toast.LENGTH_SHORT).show()
    }

    private fun loadReviews() {
        storeId?.let { id ->
            lifecycleScope.launch {
                try {
                    val response = reviewApi.getReviewsByStoreId(id) // List<ReviewEntity> 반환
                    reviews.clear() // 이전 데이터 삭제

                    // ReviewEntity를 ReviewObject로 변환
                    val reviewObjects = response.map { entity ->
                        ReviewObject(
                            ratings = entity.reviewScore, // ReviewEntity의 reviewScore를 사용
                            storeTitle = entity.review, // ReviewEntity의 review를 사용
                            reviewContent = entity.review, // ReviewEntity의 review를 사용 (필요에 따라 수정)
                            reviewTime = entity.createDate, // ReviewEntity의 createDate를 사용
                            reviewId = entity.reviewId, // ReviewEntity의 reviewId를 사용
                            storeId = id, // storeId는 Fragment에서 받은 값 사용
                            content = entity.review, // ReviewEntity의 review를 content로 사용
                            reviewImageList = entity.reviewImageList
                        )
                    }

                    reviews.addAll(reviewObjects) // 새로운 데이터 추가
                    reviewAdapter.notifyDataSetChanged() // UI 업데이트

                    if (reviews.isEmpty()) {
                        showToast("리뷰가 없습니다.")
                    } else {
                        loadReviewImagesForAllReviews()
                    }
                } catch (e: HttpException) {
                    showToast("리뷰 로드 실패: ${e.message()}")
                } catch (e: Exception) {
                    showToast("오류 발생: ${e.localizedMessage}")
                }
            }
        } ?: run {
            showToast("유효하지 않은 상점 ID입니다.")
        }
    }

    private fun loadReviewImagesForAllReviews() {
        if (reviews.isNotEmpty()) {
            reviews.forEach { review ->
                loadReviewImages(review.reviewId,review.reviewImageList)
            }
        }
    }

    private fun loadReviewImages(reviewId: Int,imageList : List<String>) {
            try{
                reviewAdapter.addImages(reviewId, imageList.map { it })
            } catch (e: Exception) {
                showToast("이미지 로드 실패: ${e.localizedMessage}")
            }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}