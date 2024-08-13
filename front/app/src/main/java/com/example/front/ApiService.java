package com.example.front;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/users/create")
    Call<UserResponse> createUser(@Body User user);

    @GET("/api/users/lists")
    Call<UserDTO> getProfileAboutUser(@Query("id") Integer userId);

    @GET("/api/users/profile")
    Call<List<PetDTO>> getPetProfileByUserId(@Query("userId") Integer userId);

    @GET("/api/users/profile/detail")
    Call<PetDetailDTO> getProfileDetailByPetId(@Query("petId") Integer petId);

    @POST("/api/pet/delete/{petId}")
    Call<Void> deleteProfile(@Path("petId") Integer petId);

//    @POST("/api/pet/delete")
//    Call<Void> deleteProfile(@Query("petId") Integer petId);

    @POST("/api/pet/modify")
    Call<PetDetailDTO> editProfile(@Query("petId") Integer petId);
//    @GET("test/connection")
//    Call<TestResponse> testConnection();
//
//    @POST("test/connectionTwo")
//    Call<String> testConnection(@Body String message);

    @POST("/api/pet/create")
    Call<UserResponse> petProfileRegistration(@Body PetCreateDTO petCreateDTO, @Query("userId") Integer userId);

    @POST("/api/pet/modify")
    Call<Void> petProfileUpdate(@Body PetCreateDTO petCreateDTO, @Query("petId") Integer petId);

//    @POST("/api/pet/create")
//    Call<UserResponse> createProfile(@Body QuestionCreateDTO questionCreateDTO, @Query("userId") Integer userId);

    // FollowingList 리사이클러뷰 뿌려주는 API
    @GET("/api/Following/List")
    Call<List<StoreFollowDTO>> getFollowingStoreList(@Query("userId") Integer userId);

    // 간략 FollowingList를 클릭하면 화면 전환 API - 필요한가? 이건 그냥 intent 발생시켜서 화면 전환하거나
    // Main 리뷰에서 클릭하면 상세 복사해서 적당히 붙여넣으면 되지 않나? 이미 API 만들긴 했는데, 다른 방식으로
    // 작동하도록 만드는 이유가 있을까?


    // 화면에서 FollowingEntitiy에 추가/삭제 Toggle 작동 API
    @POST("/api/Following/toggle")
    Call<Void> toggleFollowing(@Query("userId") Integer userId, @Query("storeId") Integer storeId);

}