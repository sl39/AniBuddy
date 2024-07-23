package org.example.anibuddy.Review;

import lombok.Getter;

import java.util.List;

@Getter
public class ReveiwCreateDto {
    private List<String> reviewImageList;
    private String nickName;
    private String review;
    private String visitedTime;
}
