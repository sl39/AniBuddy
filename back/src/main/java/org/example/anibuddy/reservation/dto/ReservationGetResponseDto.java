package org.example.anibuddy.reservation.dto;


import jakarta.persistence.criteria.CriteriaBuilder;
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
    private Integer reservationId;
    private String storePhoneNumber;
    private Integer storeId;
    private Integer state;
}
