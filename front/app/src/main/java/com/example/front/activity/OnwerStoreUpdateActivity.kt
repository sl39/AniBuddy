package com.example.front.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.example.front.R
import com.example.front.data.ApiService
import com.example.front.data.OwnerApiService
import com.example.front.data.response.LoginResponse
import com.example.front.data.response.OwnerCreateStore
import com.example.front.data.response.OwnerUpdateStore
import com.example.front.data.response.StoreOwnerDetailResponseDto
import com.example.front.databinding.ActivityOnwerStoreUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnwerStoreUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnwerStoreUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnwerStoreUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dayday = mutableListOf<String>()




        val storeId = intent.getIntExtra("storeId", -1)
        val api = ApiService.create(this)
        if (storeId != -1) {
            api.getStoreOwnerById(storeId)
                .enqueue(object : Callback<StoreOwnerDetailResponseDto> {
                    override fun onResponse(
                        call: Call<StoreOwnerDetailResponseDto>,
                        response: Response<StoreOwnerDetailResponseDto>
                    ) {
                        val data = response.body()
                        if(response.code() == 200){
                            binding.addStoreName.text = data?.storeName
                            binding.addAddress.text = data?.storeAddress
                            binding.addPhoneNumber.text = data?.storePhoneNumber
                            binding.storeInfo.setText(data?.storeInfo)
                            val category = binding.categoryList
                            val categoryList = data?.storeCategory
                            if(!categoryList.isNullOrEmpty()){
                                for(cat :String in categoryList){
                                    var c = "미용"
                                    if(cat.equals("hpspital")){
                                       c = "병원"
                                    } else if(cat.equals("training")){
                                        c = "훈련"
                                    }

                                    for(view : View in category.children){
                                        if (view is Button && view.text.equals(c)){
                                            view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFAC4A"))
                                            break
                                        }
                                    }
                                }
                            }
                            val dayTime = data?.openTime?.split(" - ")
                            Log.d("시간 시간 시간", dayTime.toString())
                            val start = dayTime?.get(0)?.trim()?.split(":")
                            val end = dayTime?.get(1)?.trim()?.split(":")
                            binding.hour.setText(start?.get(0))
                            binding.minutes.setText(start?.get(1))
                            binding.endHour.setText(end?.get(0))
                            binding.endMinutes.setText(end?.get(1))

                            val dayViews = binding.dayLinear
                            for(view in dayViews.children){
                                if(view is androidx.appcompat.widget.AppCompatButton && data?.openDay?.contains(view.text) == true ){
                                    dayday.add(view.text.toString())
                                    view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFAC4A"))

                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<StoreOwnerDetailResponseDto>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }

        val dayBtns = binding.dayLinear
        for(i in 0 .. dayBtns.childCount){
            val view = dayBtns.getChildAt(i)
            if(view is androidx.appcompat.widget.AppCompatButton){
                view.setOnClickListener{
                    val txt = view.text.toString()
                    if(txt in dayday) {
                        view.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
                        dayday.remove(txt)
                    } else {
                        view.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor("#FF8A00"))
                        dayday.add(txt)
                    }

                }
            }
        }
        binding.addStoreButton.setOnClickListener{
            val d = charArrayOf('일','월','화','수','목','금','토')
            var openDay = ""
            val hour = binding.hour.text.toString()
            val minutes = binding.minutes.text.toString()
            val endHour = binding.endHour.text.toString()
            val endMinutes = binding.endMinutes.text.toString()
            val info = binding.storeInfo.text.toString()
            if(hour=="" || minutes=="" || endHour=="" || endMinutes==""){

                return@setOnClickListener
            }
            if (dayday.size == 0){
                return@setOnClickListener
            }
            val tiempo = hour + ":" + minutes + " - " + endHour + ":" + endMinutes
            for(s: Char in d){
                for(j: String in dayday){
                    if(s.toString().equals(j)){
                        openDay += j + " " + tiempo + "//"
                    }
                }
            }

            val creatStoreApi = OwnerApiService.create(this)
                val ownerCreateStore = OwnerUpdateStore(storeId,info,openDay)
                creatStoreApi.updateStore(ownerCreateStore).enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.code() == 200){
                            val intent = Intent(this@OnwerStoreUpdateActivity, OwnerStoreDetailActivity::class.java)
                            intent.putExtra("storeId",storeId)
                            startActivity(intent)


                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("ㄷㄱㄷㄱㄷㄱㄷㄱㄷㄱㄷ","ㄷㄱㄷㄱㄷㄱㄷㄱㄷㄱㄷㄱ")
                    }

                })

        }

    }
}