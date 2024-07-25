package org.example.anibuddy.store.repository;

import org.example.anibuddy.store.dto.StoreWithDistanceDTO;
import org.example.anibuddy.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
    Optional<StoreEntity> findByStoreNameAndAddress(String storeName, String address);

//    @Query(value = "SELECT s.*, (6371000 * acos(cos(radians(:lat)) * cos(radians(s.mapy)) * cos(radians(s.mapx) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.mapy)))) AS distance " +
//            "FROM store_entity s " +
//            "HAVING distance < 1000",
//            nativeQuery = true)
//    List<Map<String, Object>> findStoresWithinDistance(double lon, double lat);
}
