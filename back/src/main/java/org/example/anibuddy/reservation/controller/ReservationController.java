package org.example.anibuddy.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.reservation.dto.ReservationCreateRequestDto;
import org.example.anibuddy.reservation.dto.ReservationCreateResponseDto;
import org.example.anibuddy.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("")
    public ReservationCreateResponseDto addReservation(@RequestBody ReservationCreateRequestDto reservation) throws Exception {
        reservationService.addReservation(reservation);
        ReservationCreateResponseDto res = ReservationCreateResponseDto.builder()
                .message("success")
                .success(true)
                .build();

        return res;
    }


}
