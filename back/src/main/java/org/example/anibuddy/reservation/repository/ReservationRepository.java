package org.example.anibuddy.reservation.repository;


import org.example.anibuddy.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
}
