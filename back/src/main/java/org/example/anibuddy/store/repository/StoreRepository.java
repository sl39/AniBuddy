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

    List<StoreEntity> findTop10ByOrderByIdDesc();

    @Query(value = "SELECT s.id as storeId, " +
            "   ROUND(6371 * acos(cos(radians(:lat)) * cos(radians(s.mapy)) * cos(radians(s.mapx) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.mapy))),1) AS distance, " +
            "   r.create_date as createdDate, r.review as review, ii.image_url as imageUrl, r.update_date as modifiedDate " +
            "FROM store_entity s " +
            "INNER JOIN (" +
            "    SELECT r.store_entity_id, MAX(r.create_date) as createDate, COUNT(r.id) as reviewCount " +
            "    FROM review_entity r " +
            "    INNER JOIN (" +
            "        SELECT i.review_entity_id, MIN(i.image_url) as image_url " +
            "        FROM review_image i " +
            "        GROUP BY i.review_entity_id) i ON r.id = i.review_entity_id " +
            "    WHERE r.create_date >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
            "      AND LENGTH(r.review) >= 20 " +
            "      AND i.image_url IS NOT NULL " +
            "    GROUP BY r.store_entity_id " +
            ") t ON s.id = t.store_entity_id " +
            "INNER JOIN review_entity r ON s.id = r.store_entity_id AND r.create_date = t.createDate " +
            "INNER JOIN (" +
            "    SELECT review_entity_id, MIN(image_url) as image_url " +
            "    FROM review_image " +
            "    GROUP BY review_entity_id" +
            ") ii ON r.id = ii.review_entity_id " +
            "INNER JOIN store_entity_store_category ca " +
            "ON s.id = ca.store_entity_list_id " +
            "WHERE (6371000 * acos(cos(radians(:lat)) * cos(radians(s.mapy)) * cos(radians(s.mapx) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.mapy)))) < 1000 " +
            "AND ca.store_category_list_id = :category " +
            "ORDER BY t.reviewCount DESC, r.create_date DESC",
            nativeQuery = true)
    List<Map<String, Object>> findStoresWithinDistance(double lon, double lat,Integer category);


    @Query(value = "SELECT s.id as storeId, " +
            "ROUND(6371 * acos(cos(radians(:lat)) * cos(radians(s.mapy)) * cos(radians(s.mapx) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.mapy))),1) AS distance, " +
            "s.store_name as storeName, COUNT(r.id) as reviewCount " +
            "FROM store_entity s " +
            "LEFT JOIN store_entity_store_category sc ON s.id = sc.store_entity_list_id " +
            "LEFT JOIN review_entity r ON s.id = r.store_entity_id " +
            "WHERE sc.store_category_list_id = :category " +
            "AND s.district = :district " +
            "GROUP BY s.id " +
            "ORDER BY reviewCount DESC",
            nativeQuery = true)
    List<Map<String, Object>> findStoresByCategoryAndDistrictWithReview(String district, Integer category, double lon, double lat);



}
