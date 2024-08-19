package org.example.anibuddy.Review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Builder
@Getter
public class ReviewDetailResponseDto {
    Integer reviewId;
    String review;
    String createDate;
    String updateDate;
    Float reviewScore;
    List<String> reviewImageList;
}
