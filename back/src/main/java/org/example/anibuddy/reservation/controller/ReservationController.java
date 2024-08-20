package org.example.anibuddy.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.Review.dto.ReviewDetailResponseDto;
import org.example.anibuddy.reservation.dto.ReservationCreateRequestDto;
import org.example.anibuddy.reservation.dto.ReservationCreateResponseDto;
import org.example.anibuddy.reservation.dto.ReservationGetResponseDto;
import org.example.anibuddy.reservation.entity.ReservationEntity;
import org.example.anibuddy.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("")
    public ReservationCreateResponseDto addReservation(@RequestBody ReservationCreateRequestDto reservation) throws Exception {
        Integer reserva = reservationService.addReservation(reservation);
        ReservationCreateResponseDto res = ReservationCreateResponseDto.builder()
                .resvationId(reserva)
                .build();

        return res;
    }

    @GetMapping("")
    public ReservationGetResponseDto getReservation(@RequestParam("reservationId") Integer reservationId) throws Exception {
        return  reservationService.getReservationDetail(reservationId);
    }



}
