package org.example.anibuddy.reservation.repository;


import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.reservation.dto.ReservationGetAllResponseDto;
import org.example.anibuddy.reservation.entity.ReservationEntity;
import org.example.anibuddy.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    Optional<ReservationEntity> findById(Integer reservationId);

    List<ReservationEntity> findAllByUserEntity(UserEntity userEntity);

    @Query(value = "SELECT r.* " +
            "FROM reservation_entity r " +
            "LEFT JOIN store_entity s " +
            "on r.store_entity_id = s.id " +
            "WHERE s.owner_entity_id = :owner "
            ,nativeQuery = true)
    List<ReservationEntity> findByOwner(@Param(value = "owner") Integer owner);
}
