package org.example.anibuddy.store.repository;

import org.example.anibuddy.store.dto.StoreWithDistanceDTO;
import org.example.anibuddy.store.entity.StoreSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface StoreSummaryRepository extends JpaRepository<StoreSummary, Integer> {
    @Query(value = "SELECT s.*, (6371000 * acos(cos(radians(:lat)) * cos(radians(s.mapy)) * cos(radians(s.mapx) - radians(:lon)) + sin(radians(:lat)) * sin(radians(s.mapy)))) AS distance " +
            "FROM store_summary s " +
            "HAVING distance < 1000",
            nativeQuery = true)
    List<Map<String, Object>> findStoresWithinDistance(double lon, double lat);
}
