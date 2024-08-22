package com.example.front

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.json.JSONObject

class ReviewWriteFragment : Fragment() {

    private var selectedRating = 0  // 선택된 평점
    private var selectedImageUri: Uri? = null // 선택된 이미지 URI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review_write, container, false)

        val reviewContentEditText = view.findViewById<EditText>(R.id.reviewContentEditText)
        val submitReviewButton = view.findViewById<Button>(R.id.submitReviewButton)
        val selectedImageView = view.findViewById<ImageView>(R.id.selectedImageView)

        // 별 이미지 클릭 리스너 설정
        val stars = listOf(
            view.findViewById<ImageView>(R.id.star1),
            view.findViewById<ImageView>(R.id.star2),
            view.findViewById<ImageView>(R.id.star3),
            view.findViewById<ImageView>(R.id.star4),
            view.findViewById<ImageView>(R.id.star5)
        )

        stars.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                selectedRating = index + 1
                updateStarRatings(stars, selectedRating)
            }
        }

        view.findViewById<Button>(R.id.uploadImageButton).setOnClickListener {
            openImagePicker()
        }

        submitReviewButton.setOnClickListener {
            val reviewContent = reviewContentEditText.text.toString()
            val reviewTime = System.currentTimeMillis().toString()

            // 리뷰 저장 로직 추가
            sendReviewToApi(reviewContent, selectedRating.toString(), reviewTime)

            Toast.makeText(context, "리뷰가 제출되었습니다.", Toast.LENGTH_SHORT).show()

            // 입력 필드 초기화
            reviewContentEditText.text.clear()
            selectedRating = 0
            updateStarRatings(stars, selectedRating) // 별 초기화
            selectedImageUri = null
            selectedImageView.setImageURI(null)
            selectedImageView.visibility = View.GONE // 이미지 뷰 숨기기
        }

        return view
    }

    private fun updateStarRatings(stars: List<ImageView>, rating: Int) {
        stars.forEachIndexed { index, imageView ->
            if (index < rating) {
                imageView.setImageResource(R.drawable.star_filled)
            } else {
                imageView.setImageResource(R.drawable.star_empty)
            }
        }
    }

    private fun openImagePicker() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_READ_STORAGE)
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_READ_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(context, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = data?.data
            view?.findViewById<ImageView>(R.id.selectedImageView)?.setImageURI(selectedImageUri)
            view?.findViewById<ImageView>(R.id.selectedImageView)?.visibility = View.VISIBLE
        }
    }

    private fun sendReviewToApi(reviewContent: String, ratings: String, reviewTime: String) {
        val reviewData = JSONObject().apply {
            put("reviewContent", reviewContent)
            put("ratings", ratings)
            put("reviewTime", reviewTime)
            selectedImageUri?.let { put("imageUri", it.toString()) }
        }
        // API 호출 로직 추가
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
        private const val REQUEST_PERMISSION_READ_STORAGE = 1002
    }
}
