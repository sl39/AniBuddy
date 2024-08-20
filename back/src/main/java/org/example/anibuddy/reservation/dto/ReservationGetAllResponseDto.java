package org.example.anibuddy.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReservationGetAllResponseDto {
    private Integer id;
    private String info;
    private Integer storeId;
    private String reservationTime;
    private String storeName;
    private String storeAddress;
}
