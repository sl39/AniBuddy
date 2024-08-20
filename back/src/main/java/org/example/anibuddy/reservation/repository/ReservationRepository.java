package org.example.anibuddy.reservation.repository;


import org.example.anibuddy.reservation.dto.ReservationGetAllResponseDto;
import org.example.anibuddy.reservation.entity.ReservationEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    Optional<ReservationEntity> findById(Integer reservationId);

    List<ReservationEntity> findAllByUserEntity(UserEntity userEntity);
}
