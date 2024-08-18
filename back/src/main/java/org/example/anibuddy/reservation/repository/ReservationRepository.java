package org.example.anibuddy.Reservation.repository;

import org.example.anibuddy.Reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByPhoneNumberAndYearAndMonthAndDay(String phoneNumber, int year, int month, int day);
}
