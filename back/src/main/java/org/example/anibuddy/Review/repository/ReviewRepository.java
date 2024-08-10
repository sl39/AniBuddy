package org.example.anibuddy.Review.repository;

import org.example.anibuddy.Review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer> {

}
