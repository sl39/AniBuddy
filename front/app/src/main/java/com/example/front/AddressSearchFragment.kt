package com.example.front

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.front.data.AddressApiService
import com.example.front.data.response.NewAddressListAreaCdSearchAll
import com.example.front.data.response.NewAddressListResponse
import com.example.front.databinding.FragmentAddressSearchBinding
import okhttp3.Call
import okhttp3.Response
import retrofit2.Callback
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddressSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddressSearchFragment : Fragment(), AddressAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentAddressSearchBinding
    private lateinit var adapter: AddressAdapter
    private var bundle = Bundle()


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
        // Inflate the layout for this fragment
        binding = FragmentAddressSearchBinding.inflate(inflater,container,false)
        adapter = AddressAdapter(mutableListOf(),this,requireContext())

        val layoutManager = LinearLayoutManager(activity)
        binding.addressResultContainer.layoutManager = layoutManager
        binding.addressResultContainer.adapter = adapter
        val api = AddressApiService.create()
        binding.searchButton.setOnClickListener {
            if (!binding.searchEditText.text.equals("")){
                val secretKey = "aEO7JxyqKxOzb9OCTBPeDUd48feyPvSGv5oeS5bwJYICwtU4fP+/p7RjLY7vcx6e5QOle1T5aZT38OfB2s66yQ=="
                Log.d("키워드 값",binding.searchEditText.text.toString())
                api.getAddress(secretKey,binding.searchEditText.text.toString(),10,1).enqueue(object:
                    Callback<NewAddressListResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<NewAddressListResponse>,
                        response: retrofit2.Response<NewAddressListResponse>
                    ) {
                        val ds = response.body()?.newAddressListAreaCdSearchAll as MutableList<NewAddressListAreaCdSearchAll>
                        adapter.updateData(ds)
                    }

                    override fun onFailure(
                        call: retrofit2.Call<NewAddressListResponse>,
                        t: Throwable
                    ) {
                        Log.d("주소 들어오나 들어오나??", t.message.toString())

                    }


                })
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddressSearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddressSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(review: NewAddressListAreaCdSearchAll) {
        val rnAdres = review.rnAdres
        val lnmAdres = review.lnmAdres
        setFragmentResult("address", bundleOf("rnAdres" to rnAdres, "lnmAdres" to lnmAdres))
        parentFragmentManager.beginTransaction().commit()
        parentFragmentManager.popBackStack()
    }

}