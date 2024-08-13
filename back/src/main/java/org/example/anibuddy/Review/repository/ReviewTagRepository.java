package org.example.anibuddy.Review.repository;

import org.example.anibuddy.Review.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewTagRepository extends JpaRepository<ReviewTag,Integer> {
    Optional<ReviewTag> findByTag(String tag);
}
