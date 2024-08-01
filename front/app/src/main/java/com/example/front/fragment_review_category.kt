package com.example.front

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.front.activity.StoreDetailActivity
import com.example.front.data.ApiService
import com.example.front.data.response.MainReviewSimpleResponseDto
import com.example.front.databinding.FragmentReviewCategoryBinding
import com.example.front.databinding.HomeReviewRecyclerviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewViewHolder(val binding: HomeReviewRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)


class ReviewAdapter(val datas: MutableList<MainReviewSimpleResponseDto>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(review: MainReviewSimpleResponseDto)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ReviewViewHolder(HomeReviewRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        val binding = (holder as ReviewViewHolder).binding
        val review = datas[position]
        if (datas[position].modifiedDate != null){
            binding.homeReviewTime.text = datas[position].modifiedDate

        } else {
            binding.homeReviewTime.text = datas[position].createdDate
        }

        binding.homeReviewText.text = datas[position].review
        binding.reviewDistance.text = datas[position].distance.toString() + "km"

        if (review.imageUrl != null){
            val imageUrl = review.imageUrl
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.reviewImage)
            Log.d("이미지 url", imageUrl.toString())
        }


        binding.root.setOnClickListener {
            itemClickListener.onItemClick(review)
        }


    }
    fun updateData(newDatas: List<MainReviewSimpleResponseDto>){
        datas.clear()
        datas.addAll(newDatas)
        notifyDataSetChanged()
    }
}




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentReviewCategory : Fragment(), ReviewAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: ReviewAdapter
    private lateinit var parentContext: Context

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


        var context : Context = requireContext()
        val api = ApiService.create(context)
        var result = arguments?.getString("category") ?: "beauty"
        Log.d("resultcategory",result.toString())

        // Inflate the layout for this fragment

        val binding = FragmentReviewCategoryBinding.inflate(inflater,container,false)
        adapter = ReviewAdapter(mutableListOf(), this)
        val layoutManager = LinearLayoutManager(activity)
        binding.fragmentReviewCategory.layoutManager = layoutManager


        binding.fragmentReviewCategory.adapter = adapter

        api.getMainStore(129.1177397,35.1560602, result).enqueue(object:
            Callback<List<MainReviewSimpleResponseDto>> {
            override fun onResponse(
                call: Call<List<MainReviewSimpleResponseDto>>,
                response: Response<List<MainReviewSimpleResponseDto>>
            ){
                when(response.code()){
                    200 ->{
                        val ds = response.body() as MutableList<MainReviewSimpleResponseDto>
                        adapter.updateData(ds)
                    }
                }
            }

            override fun onFailure(call: Call<List<MainReviewSimpleResponseDto>>, t: Throwable) {
                Log.d("api 테스트 살퍄", t.message.toString())

            }
        })



        return binding.root // 바인딩된 레이아웃 반환
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentContext = context
    }

    override fun onItemClick(review: MainReviewSimpleResponseDto) {
//        val intent : Intent = Intent(parentContext, StoreDetailActivity::class.java)
        Log.d("가게가 뭐가 나올까", review.storeId.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentReviewCategory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
