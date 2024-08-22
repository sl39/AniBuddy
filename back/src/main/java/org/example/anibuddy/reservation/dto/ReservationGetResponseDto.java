package org.example.anibuddy.reservation.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReservationGetResponseDto {
    private String reservationDateTime;
    private String storeName;
    private String storeLocation;
    private String info;

}
