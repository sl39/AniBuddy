package com.example.front

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import com.example.front.data.ApiService
import com.example.front.databinding.ActivitySearchBinding
import com.example.front.databinding.FragmentReviewCategoryBinding
import com.example.front.databinding.FragmentSearchKeywordsBinding
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_search_keywords.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_search_keywords : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSearchKeywordsBinding
    private var selectCategory = "test"
    private var selectDistrict : MutableSet<String> = mutableSetOf()
    val bundle = Bundle()
    private val fragmentSearchResult = FragmentSearchResult()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchKeywordsBinding.inflate(layoutInflater)

        val categoryBtns = binding.categoryGrid
        val districtBtns = binding.districtGrid
        setInitCategory(categoryBtns,districtBtns)
        selectColorChangeCategory(categoryBtns)
        selectColorChangeDisttrict(districtBtns)
        val searchEnterBtn = binding.searchBtn
        var context : Context = requireContext()
        val api = ApiService.create(context)

        searchEnterBtn.setOnClickListener {
            if(selectCategory.equals("test")){
                var toast = Toast.makeText(context, "카테고리를 설정해 주세요", Toast.LENGTH_SHORT)
                toast.show()
                return@setOnClickListener
            }

            var categoryRequest = "beauty"
            if(selectCategory.equals("병원")){
                categoryRequest = "hospital"
            } else if(selectCategory.equals("훈련")){
                categoryRequest = "training"
            }
            bundle.putString("categoryRequest",categoryRequest)
            bundle.putStringArrayList("selectDistrict", ArrayList(selectDistrict))
            fragmentSearchResult.bundle = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.search_content,fragmentSearchResult)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

            // Inflate the layout for this fragment
        return binding.root
    }

    fun setInitCategory(categoryBtns: GridLayout,districtBtns: GridLayout){
        for( i in 0..categoryBtns.childCount){
            val view  = categoryBtns.getChildAt(i)
            if (view is Button){
                if(view.text.equals(selectCategory)){
                    view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))

                }
            }
        }
        for( i in 0..districtBtns.childCount){
            val view  = districtBtns.getChildAt(i)
            if(view is Button){
                if (selectDistrict.contains(view.text)){
                    view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))

                }
            }

        }

        }


    fun selectColorChangeDisttrict(districtBtns : GridLayout){
        for (i in 0 until districtBtns.childCount) {
            val view = districtBtns.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    if (selectDistrict.contains(view.text.toString())) {
                        selectDistrict.remove(view.text.toString())
                        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

                    } else {
                        // Reset background for all buttons first
                        selectDistrict.add(view.text.toString())
                        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
                    }
                }
            }
        }
    }
    fun selectColorChangeCategory(districtBtns : GridLayout){
        Log.d("카테고리 카테고리", selectCategory)
        for (i in 0 until districtBtns.childCount) {
            val view = districtBtns.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    if (view.text.toString() == selectCategory) {
                        selectCategory = "test"
                        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

                    } else {
                        // Reset background for all buttons first
                        for (j in 0 until districtBtns.childCount) {
                            val button = districtBtns.getChildAt(j)
                            if (button is Button) {
                                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))

                            }
                        }
                        selectCategory = view.text.toString()
                        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
                    }
                }
            }
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_search_keywords.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_search_keywords().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}