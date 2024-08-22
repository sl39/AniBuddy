package com.example.front.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.front.R
import com.example.front.data.ApiService
import com.example.front.data.response.StoreOwnerDetailResponseDto
import com.example.front.databinding.ActivityOwnerStoreDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OwnerStoreDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOwnerStoreDetailBinding
    var storeId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeId = intent.getIntExtra("storeId", -1)
        val api = ApiService.create(this)
        if (storeId != -1){
            api.getStoreOwnerById(storeId).enqueue(object : Callback<StoreOwnerDetailResponseDto>{
                override fun onResponse(
                    call: Call<StoreOwnerDetailResponseDto>,
                    response: Response<StoreOwnerDetailResponseDto>
                ) {
                    if(response.code() == 200){
                        val data = response.body()
                        binding.addStoreName.text = data?.storeName
                        binding.addAddress.text = data?.storeAddress
                        binding.addPhoneNumber.text = data?.storePhoneNumber
                        binding.storeInfo.text = data?.storeInfo
                        binding.time.text = data?.openTime
                        val dayViews = binding.dayLinear
                        for(view in dayViews.children){
                            if(view is androidx.appcompat.widget.AppCompatButton && data?.openDay?.contains(
                                    view.text
                                ) == true ){
                                view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFAC4A"))
                            }
                        }

                        val categoryList = binding.categoryList
                        Log.d("스토어 카테고리", data?.storeCategory.toString())
                        if(data?.storeCategory != null){
                            for(category : String in data.storeCategory){
                                var cat = "미용"
                                if (category.equals("hospital")){
                                    cat = "병원"
                                } else if(category.equals("training")){
                                    cat = "훈련"
                                }
                                for(view in categoryList.children){
                                    if (view is Button && view.text.equals(cat)){
                                        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFAC4A"))
                                        break
                                    }
                                }
                            }
                        }

                        val imageContainer = binding.imageList
                        val images = data?.images
                        if(!images.isNullOrEmpty()){
                            imageContainer.removeAllViews()
                            for(imageUrl in images){
                                val imageView = ImageView(binding.root.context).apply {
                                    layoutParams = LinearLayout.LayoutParams(convertDpToPixelsTo(100F,context),convertDpToPixelsTo(100F,context)).apply {
                                        marginEnd = 8
                                    }
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                    adjustViewBounds = false
                                    binding.storeImage.clipToOutline = true

                                }
                                Glide.with(binding.root.context)
                                    .load(imageUrl)
                                    .into(imageView)
                                imageContainer.addView(imageView)

                            }

                        }


                    }
                }

                override fun onFailure(call: Call<StoreOwnerDetailResponseDto>, t: Throwable) {
                    Toast.makeText(this@OwnerStoreDetailActivity, "데이터 통신실패.", Toast.LENGTH_SHORT).show()

                }

            })
        } else {
            Toast.makeText(this@OwnerStoreDetailActivity, "스토어 아이디없음.", Toast.LENGTH_SHORT).show()
        }

        binding.addStoreButton.setOnClickListener{
            val intent = Intent(this@OwnerStoreDetailActivity,OnwerStoreUpdateActivity::class.java)
            intent.putExtra("storeId",storeId)
            startActivity(intent)
        }




    }
    fun convertDpToPixelsTo(dp: Float, context: Context): Int {
        val metrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }
}