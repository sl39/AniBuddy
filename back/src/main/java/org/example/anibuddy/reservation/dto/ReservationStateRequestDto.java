package org.example.anibuddy.reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationStateRequestDto {
    private Integer reservationId;
    private Integer state;
}
