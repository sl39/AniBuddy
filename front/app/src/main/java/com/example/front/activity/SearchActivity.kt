package com.example.front.activity

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import com.example.front.R
import com.example.front.databinding.ActivitySearchBinding
import com.example.front.fragment_search_keywords

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val fragmentManager : FragmentManager = supportFragmentManager
    private val fragmentSearchKeywords = fragment_search_keywords()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.searchTopAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        var context : Context = this@SearchActivity
        fragmentManager.beginTransaction().replace(R.id.search_content, fragmentSearchKeywords).commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_bar, menu)

        val menuItem = menu?.findItem(R.id.search_icon)
        val searchView = menuItem?.actionView as SearchView

        searchView.setIconifiedByDefault(false)
        searchView.requestFocus()
        searchView.setOnQueryTextFocusChangeListener { searchView, hasFocus ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.search_content)

            if (currentFragment !is fragment_search_keywords) {
                supportFragmentManager.popBackStackImmediate()
            }
        }



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("dkrhasdhjkfasdf","asdfasdfadsf")
                if(newText != null){
                    fragmentSearchKeywords.bundle.putString("seachKeywords",newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fragmentSearchKeywords.bundle.putString("seachKeywords",query)
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
}
