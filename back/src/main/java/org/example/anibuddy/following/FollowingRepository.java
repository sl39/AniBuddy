package org.example.anibuddy.following;

import java.util.List;

import org.example.anibuddy.store.StoreEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository <FollowingEntity, Long>{
	List<FollowingEntity> findByUserEntity(UserEntity userEntity);
	
//	List<FollowingEntity> findByUserId(Integer userId);
	
	 FollowingEntity findByUserEntityAndStoreEntity(UserEntity userEntity, StoreEntity storeEntity);
	 
	 FollowingEntity findByUserEntityAndStoreEntityAndStoreRoadaddress(UserEntity userEntity, StoreEntity storeEntity, String storeRoaddress);
	    
	 void deleteByUserEntityAndStoreEntity(UserEntity userEntity, StoreEntity storeEntity);
}
	

