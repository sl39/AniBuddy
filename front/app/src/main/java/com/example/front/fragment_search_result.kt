package com.example.front

import android.content.Context
import android.os.Bundle
import android.text.style.ClickableSpan
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.Dimension
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.front.data.ApiService
import com.example.front.data.response.MainReviewSimpleResponseDto
import com.example.front.data.response.SerachLocationCategoryResponseDto
import com.example.front.databinding.FragmentSearchKeywordsBinding
import com.example.front.databinding.FragmentSearchResultBinding
import com.example.front.databinding.SearchListRecyclerviewBinding
import com.google.android.material.shape.RoundedCornerTreatment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewHolder(val binding : SearchListRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

class SearchAdapter(val datas: MutableList<SerachLocationCategoryResponseDto>, val itemClickListener : OnItemClickListener, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(review: SerachLocationCategoryResponseDto)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        SearchViewHolder(SearchListRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun getItemCount(): Int {
        return datas.size
    }
    fun convertDpToPixelsTo(dp: Float, context: Context): Int {
        val metrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = (holder as SearchViewHolder).binding
        val search = datas[position]

        if(search.reviewCount == null){
            binding.storeReviewCount.text = "0"
        } else {
            binding.storeReviewCount.text = search.reviewCount.toString()
        }
        binding.storeDistance.text = search.distance.toString() + "KM"
        binding.storeName.text = search.storeName
        val imageContainer = binding.imageContainer
        val images = search.storeImage

        if(!images.isNullOrEmpty()){
            imageContainer.removeAllViews()
        for(imageUrl in images){
            val imageView = ImageView(binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(convertDpToPixelsTo(200F,context),convertDpToPixelsTo(200F,context)).apply {
                    marginEnd = 8
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                adjustViewBounds = false
                binding.storeImage.clipToOutline = true


                setOnClickListener {
                    itemClickListener.onItemClick(search)
                }

            }
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(imageView)
            imageContainer.addView(imageView)

            }
        } else {
            val imageView = binding.storeImage.setOnClickListener{
                itemClickListener.onItemClick(search)
            }
        }

        binding.root.setOnClickListener{
            itemClickListener.onItemClick(search)
        }
    }
    fun updateData(newDatas: List<SerachLocationCategoryResponseDto>){
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
 * Use the [fragment_search_result.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSearchResult : Fragment(), SearchAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var bundle = Bundle()
    private lateinit var adapter: SearchAdapter

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
        val binding = FragmentSearchResultBinding.inflate(inflater,container,false)

        Log.d("이거 받와지나?!","   s   categoryRequest : "  + bundle.getString("categoryRequest") + " selectDistrict : " + bundle.getStringArrayList("selectDistrict").toString()  + " seachKeywords : " + bundle.getString("seachKeywords"))
        var context = requireContext()
        val api = ApiService.create(context)
        val category = bundle.getString("categoryRequest")
        val district = bundle.getStringArrayList("selectDistrict")?.toList()
        val keyword = bundle.getString("seachKeywords") ?: ""
        adapter = SearchAdapter(mutableListOf(),this, context)
        val layoutManager = LinearLayoutManager(activity)
        binding.searchResultContainer.layoutManager = layoutManager
        binding.searchResultContainer.adapter = adapter
        if(category.equals("beauty")){
            binding.searchCategory.text = "미용"
        } else if(category.equals("hospital")){
            binding.searchCategory.text = "병원"
        } else {
            binding.searchCategory.text = "훈련"
        }
        if ((category != null) && (district != null ) ) {
            api.serachLocationCategory(129.1177397,35.1560602,category,district,keyword).enqueue(object:
            Callback<List<SerachLocationCategoryResponseDto>>{
                override fun onResponse(
                    call: Call<List<SerachLocationCategoryResponseDto>>,
                    response: Response<List<SerachLocationCategoryResponseDto>>
                ) {
                    when(response.code()){
                        200 ->{
                            val ds = response.body() as MutableList<SerachLocationCategoryResponseDto>
                            adapter.updateData(ds)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<SerachLocationCategoryResponseDto>>,
                    t: Throwable
                ) {
                    Log.d("api 테스트 살퍄 여기는 search 결과", t.message.toString())
                }
            })
        } else {
            Log.d("뭔가", "ㄷ이터가 ㅁㄴ아러ㅣ마너라ㅣㅁㅇ널ㄴ아ㅣ런아ㅣ렁나")
            return binding.root

        }

        return binding.root
    }

    override fun onItemClick(search: SerachLocationCategoryResponseDto) {
        Log.d("가게가 뭐가 나올까List", "Store 이름: "+ search.storeName + " Store category: " +  search.category + " id " + search.id)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_search_result.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSearchResult().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
