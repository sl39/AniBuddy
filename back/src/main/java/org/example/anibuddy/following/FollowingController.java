package org.example.anibuddy.following;

import java.util.List;

import org.example.anibuddy.store.dto.StoreDetailDTO;
import org.example.anibuddy.store.dto.StoreFollowDTO;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.service.StoreService;
import org.example.anibuddy.user.UserEntity;
//import org.example.anibuddy.store.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/Following")
public class FollowingController {
	
	
    private final FollowingService followingService;
    private final StoreService storeService;
	
	// List 불러오기(간략하게)
    @GetMapping("/List")
    public ResponseEntity<List<StoreFollowDTO>> getFollowingStoreList(@RequestParam(value="userId") Integer userId) {
    	List<StoreFollowDTO> stores = followingService.getFollowingStoreList(userId);
    	return ResponseEntity.ok(stores);
    }
    
    // 클릭하면 followingList 추가, 한번 더 클릭하면 제거.
    @Transactional
    @PostMapping("/toggle")
    public void toggleFollowing(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "storeId") Integer storeId, @RequestParam(value="storeCategory") String storeCategory) {
        followingService.toggleFollowing(userId, storeId, storeCategory);
    }
    
    // following 아이콘 모양 보여주는 API
    // 해당 userId, storeId, storeCategory가 FollowingEntity에 존재하면, 빨간 하트 아이콘을, 존재하지 않으면 빈 하트 아이콘을 보여주는
    @GetMapping("/Icon")
    public ResponseEntity<Boolean> checkFollowing(
            @RequestParam(value="userId") UserEntity userEntity,
            @RequestParam(value="storeId") StoreEntity storeEntity,
            @RequestParam(value="storeCategory") String storeCategory) {
        boolean isFollowing = followingService.isFollowing(userEntity, storeEntity, storeCategory);
        return ResponseEntity.ok(isFollowing);
    }

    // follow List 중 하나 클릭화면 storeDetail로 리다이렉트
    @GetMapping("/redirectToapi/store/{storeId}")
    public String redirectToStore(@RequestParam("storeId") Integer storeId) {
        return "redirect:/api/store/" + storeId;
    }
}