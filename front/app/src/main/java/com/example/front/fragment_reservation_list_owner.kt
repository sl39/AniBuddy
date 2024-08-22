package com.example.front

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.front.databinding.FragmentReservationListOwnerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_reservation_list_owner.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_reservation_list_owner : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding : FragmentReservationListOwnerBinding
    private var fragmentReservationIngOwnerList = fragment_reservation_ing_owner_list()
    private var fragmentReservationCompleteOwnerList = fragment_reservation_complete_owner_list()
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
        binding = FragmentReservationListOwnerBinding.inflate(inflater,container,false)
        binding.reservationIng.setOnClickListener {
            binding.reservationIng.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFAC4A"))
            binding.reservationComplete.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            childFragmentManager.beginTransaction().replace(R.id.fragment_res_owner,fragmentReservationIngOwnerList).commitAllowingStateLoss()
        }
        binding.reservationComplete.setOnClickListener {
            binding.reservationIng.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
            binding.reservationComplete.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFAC4A"))
            childFragmentManager.beginTransaction().replace(R.id.fragment_res_owner,fragmentReservationCompleteOwnerList).commitAllowingStateLoss()

        }
        binding.reservationIng.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFAC4A"))
        binding.reservationComplete.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9"))
        childFragmentManager.beginTransaction().replace(R.id.fragment_res_owner,fragmentReservationIngOwnerList).commitAllowingStateLoss()


        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_reservation_list_owner.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_reservation_list_owner().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}