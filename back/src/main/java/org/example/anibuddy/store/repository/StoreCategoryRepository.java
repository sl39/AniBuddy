package org.example.anibuddy.store.repository;

import org.example.anibuddy.store.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Integer> {
}
