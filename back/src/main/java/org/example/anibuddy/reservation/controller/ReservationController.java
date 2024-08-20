package org.example.anibuddy.reservation;

import org.example.anibuddy.Reservation.ReservationEntity;
import org.example.anibuddy.Reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    
    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping
    public ResponseEntity<ReservationEntity> createReservation(@RequestBody ReservationEntity reservationEntity) {
        try {
            ReservationEntity savedReservation = reservationService.saveReservation(reservationEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
        } catch (Exception e) {
            // 적절한 예외 처리 및 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 모든 예약 조회
    @GetMapping
    public ResponseEntity<List<ReservationEntity>> getAllReservations() {
        try {
            List<ReservationEntity> reservations = reservationService.getAllReservations();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
