package org.example.anibuddy.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ReservationCreateResponseDto {
    Boolean success;
    String message;
}
