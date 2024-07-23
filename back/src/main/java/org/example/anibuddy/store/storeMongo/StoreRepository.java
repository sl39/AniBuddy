package org.example.anibuddy.store.storeMongo;

import org.example.anibuddy.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    Optional<Store> findByStoreNameAndMapxAndMapy(String storeName, Integer mapx, Integer mapy);
    Optional<Store> findByStoreName(String storeName);

}
