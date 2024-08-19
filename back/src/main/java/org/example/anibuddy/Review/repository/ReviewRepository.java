package org.example.anibuddy.Review.repository;

import org.example.anibuddy.Review.entity.ReviewEntity;
import org.example.anibuddy.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer> {

    List<ReviewEntity> findByStoreEntity(StoreEntity storeEntity);
}
