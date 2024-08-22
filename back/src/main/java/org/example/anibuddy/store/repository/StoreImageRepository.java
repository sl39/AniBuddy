package org.example.anibuddy.store.repository;

import org.example.anibuddy.store.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreImageRepository extends JpaRepository<StoreImage,Integer> {
    @Query(value = "SELECT i.image_url " +
            "FROM store_image i " +
            "WHERE i.store_entity_id = :storeId",
            nativeQuery = true
    )
    Optional<List<String>> findAllByStoreId(@Param("storeId") Integer storeId);
}
