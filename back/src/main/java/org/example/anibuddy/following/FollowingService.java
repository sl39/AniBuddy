package org.example.anibuddy.following;

import java.util.List;
import java.util.stream.Collectors;

import org.example.anibuddy.store.dto.StoreDetailDTO;
import org.example.anibuddy.store.dto.StoreFollowDTO;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowingService {

	private FollowingRepository followingRepository;
	private StoreRepository storeRepository;
	private UserRepository userRepository;



    // 버튼 누르면 리스트에 추가, 한 번 더 누르면 삭제

	public void toggleFollowing(Integer userId, Integer storeId) {
	        // storeId로 StoreEntity 조회
        StoreEntity store = storeRepository.findById(storeId)
        		.orElseThrow(() -> new EntityNotFoundException("Store with ID " + storeId + " not found"));

        String storeRoadaddress = store.getRoadaddress();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
	    FollowingEntity existingFollowing = followingRepository.findByUserEntityAndStoreEntityAndStoreRoadaddress(user, store, storeRoadaddress);
	    if (existingFollowing != null) {
	        followingRepository.delete(existingFollowing);
	    } else {	
	        FollowingEntity following = new FollowingEntity();
	        following.setUserEntity(user);
	        following.setStoreEntity(store);
	        following.setStoreRoadaddress(storeRoadaddress);
	        followingRepository.save(following);
	    }
	}

	// userId로 FollowEntity 조회
	public List<StoreFollowDTO> getFollowingStoreList(Integer userId) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

		List<FollowingEntity> followings = followingRepository.findByUserEntity(user);

		return followings.stream()
	            .map(following -> {
	                StoreEntity store = following.getStoreEntity();
	                StoreFollowDTO storeDTO = new StoreFollowDTO();
	                storeDTO.setId(store.getId());
	                storeDTO.setStoreName(store.getStoreName());
	                storeDTO.setAddress(store.getAddress());
	                storeDTO.setRoadaddress(store.getRoadaddress());
	                storeDTO.setStoreImageList(store.getStoreImageList());
	                return storeDTO;
	            })
	            .collect(Collectors.toList());
	    }

public StoreDetailDTO getStoreDetailDTO(Integer id) {
	StoreEntity storeEntity = storeRepository.findById(id)
	.orElseThrow(() -> new IllegalArgumentException("해당 가게가 없습니다!"));
	
	return convertToDetailDTO(storeEntity);
	}

private StoreDetailDTO convertToDetailDTO(StoreEntity store) {
	StoreDetailDTO storeDetailDTO = new StoreDetailDTO(store.getStoreName(), store.getAddress(), store.getRoadaddress(), store.getStoreInfo(), store.getPhoneNumber(), store.getOpenday(), store.getDistrict(), store.getStoreImageList(), store.getMapx(), store.getMapy(), store.getId());
	return storeDetailDTO;
	}
}




//		public List<FollowingDTO> getFollowingList(Integer userId) {
//			List<FollowingEntity> followwingEntity = followingRepository.findByUserId(userId);		
//		UserEntity user = userRepository.findById(userId)
//				 .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
//
//        return followingRepository.findByUserEntity(user);


//// 필요한 것 3. FollowingENtity 생성
//public void createFollowing(@RequestBody FollowingDTO followingDTO, @RequestParam(value = "storeId") Integer storeId, @RequestParam(value = "userId") Integer userId) {
//	UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//	StoreEntity storeEntity = storeRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
//	FollowingEntity following = new FollowingEntity();
//	following.setStoreCategory(followingDTO.getStoreCategory());
//	following.setUserEntity(userEntity);
//	following.setStoreEntity(storeEntity);
//	this.followingRepository.save(following);
//}


