package com.example.front.data.response

import com.google.android.datatransport.cct.StringMerger

data class OwnerStoreListResponse(
    val id : Int,
    val storeName: String,
    val storeAddress: String
)

data class OwnerCreateStore(
    val name : String,
    val address: String,
    val roadAddress : String,
    val info : String,
    val phone_number : String,
    val openDay: String,
    val mapx: Double,
    val mapy: Double,
    val district : String,
    val storeImageList: List<String>,
    val category: List<String>
)

data class OwnerUpdateStore(
    val storeId: Int,
    val info : String,
    val openDay: String,

)


data class LocationResponse(
    val meta: Meta,
    val documents: List<Document>
)

data class Meta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)

data class Document(
    val address_name: String,
    val y: String,
    val x: String,
    val address_type: String,
    val address: Address,
    val road_address: RoadAddress?
)

data class Address(
    val address_name: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val region_3depth_h_name: String?,
    val h_code: String?,
    val b_code: String?,
    val mountain_yn: String,
    val main_address_no: String,
    val sub_address_no: String?,
    val x: String,
    val y: String
)

data class RoadAddress(
    val address_name: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val road_name: String,
    val underground_yn: String,
    val main_building_no: String,
    val sub_building_no: String?,
    val building_name: String?,
    val zone_no: String,
    val y: String,
    val x: String
)


data class StoreOwnerDetailResponseDto(
    val storeName :String,
    val  storeAddress: String,
    val storePhoneNumber : String,
    val storeInfo : String,
    val openDay : List<String>,
    val openTime : String,
    val storeCategory : List<String>,
    val images : List<String>,
)