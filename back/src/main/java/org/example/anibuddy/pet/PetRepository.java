package org.example.anibuddy.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetRepository extends JpaRepository <PetEntity, Integer> {
	List<PetEntity> findByUserEntityId(Integer id);
	// UserId랑 연결된 PetEntity List형태로 불러오기 위해 Repository에 정의.

//	@Query("SELECT COUNT(p) FROM PetEntity p WHERE p.mainCategory =?1")
//	int countByMainCategory(String mainCategory);
//	// mainCategory에서 선택하는대로 petId가 다르게 만들어지도록.
}