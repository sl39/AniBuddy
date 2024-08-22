package org.example.anibuddy.following;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.store.dto.StoreDetailDTO;
import org.example.anibuddy.store.dto.StoreFollowDTO;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowingService {

	private final FollowingRepository followingRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	
	
	public boolean isFollowing(Integer userEntity, Integer storeEntity,String storeCategory) {
		 	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	        Integer userId = userDetails.getUserId();
	        UserEntity user = userRepository.findById(userId)
	                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다!"));
	        Optional<StoreEntity> store = storeRepository.findById(storeEntity);
	        if(store.isEmpty()) {
	        	 throw new UsernameNotFoundException("User not found");
	        }
	        StoreEntity store1 = store.get();
	        return followingRepository.existsByUserEntityAndStoreEntityAndStoreCategory(user, store1, storeCategory);
    }
	
    // 버튼 누르면 리스트에 추가, 한 번 더 누르면 삭제

	public void toggleFollowing(Integer id, Integer storeId, String storeCategory) {
	        // storeId로 StoreEntity 조회
		// store id 조회
        StoreEntity storeEntity = storeRepository.findById(storeId)
        		.orElseThrow(() -> new EntityNotFoundException("Store with ID " + storeId + " not found"));
        // user id 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();
        
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        
	    FollowingEntity existingFollowing = followingRepository.findByUserEntityAndStoreEntityAndStoreCategory(userEntity, storeEntity, storeCategory);
	    if (existingFollowing != null) {
	        followingRepository.delete(existingFollowing);
	    } else {	
	        FollowingEntity following = new FollowingEntity();
	        following.setUserEntity(userEntity);
	        following.setStoreEntity(storeEntity);
	        following.setStoreCategory(storeCategory);
	        followingRepository.save(following);
	    }
	}

	// userId로 FollowEntity 조회
	public List<StoreFollowDTO> getFollowingStoreList(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();
		
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
	                storeDTO.setStoreCategory(following.getStoreCategory());
	                storeDTO.setStoreImageList(store.getStoreImageList());
	                return storeDTO;
	            })
	            .collect(Collectors.toList());
	    }
//
//public StoreDetailDTO getStoreDetailDTO(Integer id) {
//	StoreEntity storeEntity = storeRepository.findById(id)
//	.orElseThrow(() -> new IllegalArgumentException("해당 가게가 없습니다!"));
//	
//	return convertToDetailDTO(storeEntity);
//	}
//
//private StoreDetailDTO convertToDetailDTO(StoreEntity store) {
//	StoreDetailDTO storeDetailDTO = new StoreDetailDTO(store.getStoreName(), store.getAddress(), store.getRoadaddress(), store.getStoreInfo(), store.getPhoneNumber(), store.getOpenday(), store.getDistrict(), store.getStoreImageList(), store.getMapx(), store.getMapy(), store.getId());
//	return storeDetailDTO;
//	}
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


