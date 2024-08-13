package org.example.anibuddy.store.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainReviewSimpleResponseDto {
    private Integer storeId;
    private double distance;
    private LocalDate createdDate;
    private String review;
    private String imageUrl;
    private LocalDate modifiedDate;
    private String category;


}
