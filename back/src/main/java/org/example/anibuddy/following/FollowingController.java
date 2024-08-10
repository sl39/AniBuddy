package org.example.anibuddy.following;

import java.util.List;

import org.example.anibuddy.store.StoreDetailDTO;
import org.example.anibuddy.store.StoreFollowDTO;
//import org.example.anibuddy.store.StoreService;
import org.example.anibuddy.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Following")
public class FollowingController {
	
	@Autowired
    private FollowingService followingService;
	
//	@Autowired
//    private StoreService storeService;
	
	// List 불러오기(간략하게)
    @GetMapping("/List")
    public ResponseEntity<List<StoreFollowDTO>> getFollowingStoreList(@RequestParam(value="userId") Integer userId) {
    	List<StoreFollowDTO> stores = followingService.getFollowingStoreList(userId);
    	return ResponseEntity.ok(stores);
    }
    
    
//	    		
//    		List<PetDTO> pets = userService.getPetByUserId(userId);
//    		return ResponseEntity.ok(pets);
//    }


    // 클릭하면 followingList 추가, 한번 더 클릭하면 제거.
    @Transactional
    @PostMapping("/toggle")
    public void toggleFollowing(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "storeId") Integer storeId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        followingService.toggleFollowing(user, storeId);
    }
    
    // List 클릭하면 가게 상세 페이지로 전환
    @GetMapping("/Listdetail")
    public ResponseEntity<StoreDetailDTO> getStoreDetailByStoreId(@RequestParam(value = "id") Integer id) {
    	StoreDetailDTO storeDetail = followingService.getStoreDetailDTO(id);
    	return ResponseEntity.ok(storeDetail);
    }   
}