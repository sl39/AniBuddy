package com.example.front.activity

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.front.R
import com.example.front.data.ApiService
import com.example.front.data.response.SerachLocationCategoryResponseDto
import com.example.front.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    var selectCategory = ""
    var selectDistrict = ""
    var seachKeywords = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.searchTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val categoryBtns = binding.categoryGrid
        val districtBtns = binding.districtGrid
        val categorySelect = binding.categorySelect
        val districtSelect = binding.districtSelect
        categorySelect.visibility = View.INVISIBLE
        districtSelect.visibility = View.INVISIBLE
        selectColorChangeCategory(categoryBtns,categorySelect)
        selectColorChangeDisttrict(districtBtns,districtSelect)

        val searchEnterBtn = binding.searchBtn
        var context : Context = this@SearchActivity
        val api = ApiService.create(context)
        searchEnterBtn.setOnClickListener {
            api.serachLocationCategory(128.9041947,
                35.0830432,
                selectCategory,
                selectDistrict,
                seachKeywords).enqueue(object: Callback<List<SerachLocationCategoryResponseDto>>{
                override fun onResponse(
                    call: Call<List<SerachLocationCategoryResponseDto>>,
                    response: Response<List<SerachLocationCategoryResponseDto>>
                ) {
                    when(response.code()){
                        200 ->{
                            val ds = response.body() as MutableList<List<SerachLocationCategoryResponseDto>>
                            Log.d("안녕하ㅔ요", ds.toString())
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<SerachLocationCategoryResponseDto>>,
                    t: Throwable
                ) {
                    Log.d("api 테스트 살퍄", t.message.toString())
                }
            })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_bar, menu)

        val menuItem = menu?.findItem(R.id.search_icon)
        val searchView = menuItem?.actionView as SearchView

        searchView.setIconifiedByDefault(false)
        searchView.requestFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    seachKeywords = newText
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.d("Query", query)
                }
                return true
            }
        })
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun selectColorChangeDisttrict(districtBtns : GridLayout , btn: Button){
        for (i in 0 until districtBtns.childCount) {
            val view = districtBtns.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    if (view.text.toString() == selectDistrict) {
                        selectDistrict = "test"
                        view.backgroundTintList = ColorStateList.valueOf(android.R.drawable.btn_default)

                    } else {
                        // Reset background for all buttons first
                        for (j in 0 until districtBtns.childCount) {
                            val button = districtBtns.getChildAt(j)
                            if (button is Button) {
                                button.backgroundTintList = ColorStateList.valueOf(android.R.drawable.btn_default)

                            }
                        }
                        selectDistrict = view.text.toString()
                        Log.d("오나 마나", view.text.toString())
                        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4BB6E8"))
                    }
                    SelectChange(btn,selectDistrict)
                }
            }
        }
    }
    fun selectColorChangeCategory(districtBtns : GridLayout, btn: Button){
        for (i in 0 until districtBtns.childCount) {
            val view = districtBtns.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    if (view.text.toString() == selectCategory) {
                        selectCategory = "test"
                        view.backgroundTintList = ColorStateList.valueOf(android.R.drawable.btn_default)

                    } else {
                        // Reset background for all buttons first
                        for (j in 0 until districtBtns.childCount) {
                            val button = districtBtns.getChildAt(j)
                            if (button is Button) {
                                button.backgroundTintList = ColorStateList.valueOf(android.R.drawable.btn_default)

                            }
                        }
                        val categoryValue = view.text.toString()
                        if(categoryValue.equals("미용")){
                            selectCategory = "beauty"
                        } else if(categoryValue.equals("병원")){
                            selectCategory = "hospital"
                        } else {
                            selectCategory = "training"
                        }
                        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4BB6E8"))
                    }
                    SelectChange(btn,selectCategory)

                }
            }
        }
    }
    fun SelectChange(btn : Button, select : String){
        if (select.equals("test")){
            btn.visibility = View.INVISIBLE
        } else{
            btn.visibility = View.VISIBLE
            btn.text = select
        }


    }

}
