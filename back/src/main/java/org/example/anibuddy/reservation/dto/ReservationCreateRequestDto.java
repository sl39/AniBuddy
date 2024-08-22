package org.example.anibuddy.reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationCreateRequestDto {
    private String reservationTime;
    private String info;
    private Integer storeId;

}
