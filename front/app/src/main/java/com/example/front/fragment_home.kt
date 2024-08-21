package com.example.front

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.front.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class fragment_home : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val fragmentReviewCategory = FragmentReviewCategory()
    lateinit var binding: FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.reviewBeauty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
        binding.reviewHospital.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
        binding.reviewTraining.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))

        // Inflate the layout for this fragment
        binding.reviewBeauty.setOnClickListener {
            val fragmentReviewCategory = FragmentReviewCategory()
            val bundle = Bundle()
            bundle.putString("category", "beauty")
            fragmentReviewCategory.arguments = bundle
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmenthome, fragmentReviewCategory)
                .commitAllowingStateLoss()
            binding.reviewBeauty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
            binding.reviewHospital.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            binding.reviewTraining.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
        }

        binding.reviewHospital.setOnClickListener {
            val fragmentReviewCategory = FragmentReviewCategory()
            val bundle = Bundle()
            bundle.putString("category", "hospital")
            fragmentReviewCategory.arguments = bundle
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmenthome, fragmentReviewCategory)
                .commitAllowingStateLoss()
            binding.reviewBeauty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            binding.reviewHospital.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
            binding.reviewTraining.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
        }

        binding.reviewTraining.setOnClickListener {
            val fragmentReviewCategory = FragmentReviewCategory()
            val bundle = Bundle()
            bundle.putString("category", "training")
            fragmentReviewCategory.arguments = bundle
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmenthome, fragmentReviewCategory)
                .commitAllowingStateLoss()
            binding.reviewBeauty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            binding.reviewHospital.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            binding.reviewTraining.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Now it's safe to perform fragment transactions
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmenthome, fragmentReviewCategory)
            .commitAllowingStateLoss()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
