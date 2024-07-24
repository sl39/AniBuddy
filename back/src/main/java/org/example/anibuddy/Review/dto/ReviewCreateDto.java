package org.example.anibuddy.Review.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewCreateDto {
    private String nickName;
    private String review;
    private String visitedTime;
    private List<String> reviewImageList;
    private String storeName;
    private String storeAddress;
}
