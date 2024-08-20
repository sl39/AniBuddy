package org.example.anibuddy.reservation;

import org.example.anibuddy.Reservation.ReservationEntity;
import org.example.anibuddy.Reservation.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    // 예약 저장
    public ReservationEntity saveReservation(ReservationEntity reservationEntity) {
        // 예외 처리 및 유효성 검증 로직 추가 가능
        logger.info("Saving reservation: {}", reservationEntity);
        return reservationRepository.save(reservationEntity);
    }

    // 모든 예약 조회
    public List<ReservationEntity> getAllReservations() {
        logger.info("Fetching all reservations");
        return reservationRepository.findAll();
    }
}
