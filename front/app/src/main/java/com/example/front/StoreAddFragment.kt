package com.example.front

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.front.databinding.FragmentStoreAddBinding
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreAddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var bindig : FragmentStoreAddBinding
    private val AddressSearchFragment = AddressSearchFragment()
    private val StoreAddTwoFragment = StoreAddTwoFragment()
    private var lnmAdres : String? = ""
    private var rnAdres : String? = ""
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
        val category = mutableListOf<String>()
        bindig = FragmentStoreAddBinding.inflate(inflater,container,false)
        bindig.addAddress.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                parentFragmentManager.beginTransaction().replace(R.id.ownerActivity,AddressSearchFragment).addToBackStack(null).commitAllowingStateLoss()
            }
        }

        bindig.addBeauty.setOnClickListener{
            if("beauty" in category){
                category.remove("beauty")
                bindig.addBeauty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            } else {
                category.add("beauty")
                bindig.addBeauty.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))

            }
        }
        bindig.addHospital.setOnClickListener{
            if("hospital" in category){
                category.remove("hospital")
                bindig.addHospital.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            } else {
                category.add("hospital")
                bindig.addHospital.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))

            }
        }
        bindig.addTraining.setOnClickListener{
            if("training" in category){
                category.remove("training")
                bindig.addTraining.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            } else {
                category.add("training")
                bindig.addTraining.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF8A00"))

            }
        }


        checkId()
        bindig.addStoreButton.setOnClickListener{
            val detailAddress = bindig.addAddressDetail.text.toString()
            val name = bindig.addStoreName.text.toString()
            val phone_number = bindig.addPhoneNumber.text.toString()

            if(name == ""){
                return@setOnClickListener
            }
            if(detailAddress =="" || lnmAdres == ""){
                return@setOnClickListener
            }
            if(phone_number == ""){
                return@setOnClickListener
            }
            if(category.size == 0){
                return@setOnClickListener
            }

            bundle.putString("rnAdres",rnAdres)
            bundle.putString("lnmAdres",lnmAdres)
            bundle.putString("name",name)
            bundle.putString("detailAddress", detailAddress)
            bundle.putString("phone_number",phone_number)
            bundle.putStringArrayList("category",category as ArrayList<String>)
            StoreAddTwoFragment.bundle = bundle
            parentFragmentManager.beginTransaction().replace(R.id.ownerActivity,StoreAddTwoFragment).addToBackStack(null).commitAllowingStateLoss()

        }


        // Inflate the layout for this fragment
        return bindig.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StoreAddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoreAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    fun checkId(){
        setFragmentResultListener("address"){requestKey, bundle ->
            lnmAdres = bundle.getString("lnmAdres").toString()
            rnAdres = bundle.getString("rnAdres").toString()
            bindig.addAddress.setText(rnAdres)

        }
    }
}