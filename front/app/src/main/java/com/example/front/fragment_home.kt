package com.example.front

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.front.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class fragment_home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val fragmentReviewCategory = FragmentReviewCategory()
    lateinit var binding: FragmentHomeBinding
    val bundle = Bundle()


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
        // Inflate the layout for this fragment
        binding.reviewBeauty.setOnClickListener {
            Log.d("Category", "beauty")
            bundle.putString("category", "beauty")
            fragmentReviewCategory.arguments = bundle
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmenthome, fragmentReviewCategory)
                .commitAllowingStateLoss()
        }

        binding.reviewHospital.setOnClickListener {
            Log.d("Category", "hospital")
            bundle.putString("category", "hospital")
            fragmentReviewCategory.arguments = bundle
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmenthome, fragmentReviewCategory)
                .commitAllowingStateLoss()
        }

        binding.reviewTraining.setOnClickListener {
            Log.d("Category", "training")
            bundle.putString("category", "training")
            fragmentReviewCategory.arguments = bundle
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmenthome, fragmentReviewCategory)
                .commitAllowingStateLoss()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentReviewCategory.arguments = bundle
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
