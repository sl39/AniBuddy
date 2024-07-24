package org.example.anibuddy.store.repository;

import org.example.anibuddy.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
    Optional<StoreEntity> findByStoreNameAndAddress(String storeName, String address);
}
