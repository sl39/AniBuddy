package org.example.anibuddy.following;

import java.util.List;

import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository <FollowingEntity, Long>{
	List<FollowingEntity> findByUserEntity(UserEntity userEntity);
	
//	List<FollowingEntity> findByUserId(Integer userId);
	
	 FollowingEntity findByUserEntityAndStoreEntity(UserEntity userEntity, StoreEntity storeEntity);
	 
	 FollowingEntity findByUserEntityAndStoreEntityAndStoreCategory(UserEntity userEntity, StoreEntity storeEntity, String storeCategory);
	    
	 void deleteByUserEntityAndStoreEntity(UserEntity userEntity, StoreEntity storeEntity);

	boolean existsByUserEntityAndStoreEntityAndStoreCategory(UserEntity userEntity, StoreEntity storeEntity, String storeCategory);

}
	

