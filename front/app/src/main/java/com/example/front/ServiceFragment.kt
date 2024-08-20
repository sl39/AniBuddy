package com.example.front

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.front.retrofit.RetrofitService
import com.example.front.retrofit.Store
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import retrofit2.HttpException
import retrofit2.Response

class ServiceFragment : Fragment() {

    private var storeId: Int? = null
    private var store: Store? = null // Store 객체를 위한 nullable 변수
    private lateinit var storeInfoTextView: TextView // TextView를 위한 변수

    companion object {
        fun newInstance(storeId: Int): ServiceFragment {
            val fragment = ServiceFragment()
            val args = Bundle()
            args.putInt("STORE_ID", storeId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt("STORE_ID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ServiceFragment", "onCreateView 호출됨")
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ServiceFragment", "onViewCreated 호출됨")

        storeInfoTextView = view.findViewById(R.id.storeInfoTextView)

        // 초기 UI 업데이트
        updateUI("정보를 불러오는 중입니다.")

        // Fragment에서 Store ID를 인자로 받아오기
        val storeId = arguments?.getInt("STORE_ID") ?: -1
        Log.d("ServiceFragment", "전달된 Store ID: $storeId")

        if (storeId != -1) {
            fetchStoreInfo(storeId) // Store ID를 사용하여 정보 가져오기
        } else {
            updateUI("유효하지 않은 매장 ID입니다.")
        }
    }

    private fun fetchStoreInfo(storeId: Int) {
        val storeApi = RetrofitService.storeService(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response: Response<Store> = storeApi.getStoreInfo(storeId)
                Log.d("ServiceFragment", response.code().toString())
                if (response.isSuccessful) {
                    store = response.body()
                    if (isAdded) {
                        // 필요한 정보만 추출하여 UI 업데이트
                        store?.let {
                            // storeInfo를 줄바꿈 문자로 나누어 표시
                            val storeInfo = it.storeInfo?.replace(".", "\n")
                            updateUI(storeInfo ?: "매장 정보가 없습니다.")
                        } ?: updateUI("매장 정보가 없습니다.")
                    }
                } else {
                    if (isAdded) {
                        val errorMessage = response.errorBody()?.string() ?: response.message()
                        Log.e("ServiceFragment", "API 호출 오류: $errorMessage")
                        updateUI("오류 발생: $errorMessage")
                    }
                }
            } catch (e: HttpException) {
                if (isAdded) {
                    Log.e("ServiceFragment", "서버 오류: ${e.message}")
                    updateUI("서버 오류: ${e.message}")
                }
            } catch (e: Exception) {
                if (isAdded) {
                    Log.e("ServiceFragment", "일반 오류: ${e.message}")
                    updateUI("오류 발생: ${e.message}")
                }
            }
        }
    }



    private fun updateUI(message: String) {
        Log.d("ServiceFragment", "UI 업데이트: $message")
        storeInfoTextView.text = message
    }
}
