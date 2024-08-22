package org.example.anibuddy.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.Review.dto.ReviewDetailResponseDto;
import org.example.anibuddy.reservation.dto.*;
import org.example.anibuddy.reservation.entity.ReservationEntity;
import org.example.anibuddy.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @PutMapping("")
    public ReservationCreateResponseDto updateReservation(@RequestBody ReservationUpdateRequestDto reservation) throws Exception {
        Integer reserva = reservationService.updateReservation(reservation);
        ReservationCreateResponseDto res = ReservationCreateResponseDto.builder()
                .resvationId(reserva)
                .build();

        return res;
    }

    @DeleteMapping("")
    public Map<String,String> DeleteReservation(@RequestParam("reservationId") Integer reservationId) throws Exception {
        reservationService.deleteReservationDetail(reservationId);
        Map<String,String> res = new HashMap<>();
        res.put("message", "success");
        return res;
    }

    @GetMapping("/all")
    public List<ReservationGetAllResponseDto> getAllReservations() throws Exception {
        return reservationService.getAllReservation();
    }

    @PutMapping("/state")
    public ReservationCreateResponseDto updateReservationState(@RequestBody ReservationStateRequestDto reservation) throws Exception {
        System.out.println(reservation.getReservationId());
        reservationService.updateState(reservation);
        ReservationCreateResponseDto res = ReservationCreateResponseDto.builder()
                .resvationId(reservation.getReservationId())
                .build();
        return res;
    }


    //  owner
    @GetMapping("/owner/all")
    public List<ReservationGetAllResponseDto> getAllOwnerReservations() throws Exception {
        return reservationService.getAllOwnerReservation();
    }

    @GetMapping("/owner")
    public ReservationGetResponseDto getOwnerReservation(@RequestParam("reservationId") Integer reservationId) throws Exception {
        return  reservationService.getOwnerReservationDetail(reservationId);
    }


}
