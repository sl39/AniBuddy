package org.example.anibuddy.reservation.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationUpdateRequestDto {
    private Integer reservationId;
    private String reservationTime;
    private String info;
    private Integer storeId;
}
