package com.example.front

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.front.retrofit.RetrofitService
import com.example.front.retrofit.Store
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.launch
import retrofit2.Response

class StoreInfoFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private var storeId: Int? = null
    private var storeName: String? = null
    private var storePhoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storeId = it.getInt(ARG_STORE_ID)
        }
    }

    companion object {
        private const val ARG_STORE_ID = "storeId"

        @JvmStatic
        fun newInstance(storeId: Int): StoreInfoFragment {
            return StoreInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_STORE_ID, storeId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        storeId = arguments?.getInt(ARG_STORE_ID)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        storeId?.let { fetchStoreInfo(it) } ?: run {
            Toast.makeText(context, "매장 ID가 없습니다.", Toast.LENGTH_SHORT).show()
        }

        naverMap.setOnMapClickListener { pointF, _ ->
            val latLng = naverMap.projection.fromScreenLocation(pointF)
            openNaverMap(latLng)
        }
    }

    private fun fetchStoreInfo(storeId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response: Response<Store> = RetrofitService.storeService.getStoreInfo(storeId)
                if (response.isSuccessful) {
                    response.body()?.let { store ->
                        updateUI(store)
                    } ?: run {
                        Toast.makeText(context, "매장 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("StoreInfoFragment", "Error: ${response.code()}")
                    Toast.makeText(context, "매장 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("StoreInfoFragment", "Error fetching store info", e)
                Toast.makeText(context, "매장 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(store: Store) {
        if (!::naverMap.isInitialized) {
            Log.e("StoreInfoFragment", "NaverMap is not initialized")
            return
        }

        // 전화번호 저장
        storePhoneNumber = store.phoneNumber

        view?.findViewById<TextView>(R.id.storePhoneTextView)?.text = store.phoneNumber
        view?.findViewById<TextView>(R.id.storeOpenDayTextView)?.text = store.openday
        view?.findViewById<TextView>(R.id.storeAddressTextView)?.text = store.roadaddress

        val storeLocation = LatLng(store.mapx ?: 0.0, store.mapy ?: 0.0)
        addMarker(storeLocation)
        moveCamera(storeLocation)
    }

    private fun goToReservationActivity() {
        storePhoneNumber?.let {
            // ReservationActivity로 데이터 전달
            val intent = Intent(activity, ReservationActivity::class.java).apply {
                putExtra("storeName", view?.findViewById<TextView>(R.id.storeNameTextView)?.text.toString())  // 매장 이름
                putExtra("storeAddress", view?.findViewById<TextView>(R.id.storeAddressTextView)?.text.toString())  // 매장 주소
            }
            startActivity(intent)
        }
    }

    private fun addMarker(location: LatLng) {
        Marker().apply {
            position = location
            map = naverMap
        }
    }

    private fun moveCamera(location: LatLng) {
        naverMap.moveCamera(CameraUpdate.scrollTo(location))
    }

    private fun openNaverMap(latLng: LatLng) {
        val uri = Uri.parse("nmap://place?lat=${latLng.latitude}&lng=${latLng.longitude}")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    fun getStoreName(): String? {
        return storeName
    }
    fun getStorePhoneNumber(): String? {
        return storePhoneNumber
    }
}
