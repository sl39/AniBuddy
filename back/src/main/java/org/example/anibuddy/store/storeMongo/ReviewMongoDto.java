package org.example.anibuddy.store.storeMongo;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewMongoDto {
    private String nickName;
    private String review;
    private List<String> reviewImageList;
    private List<String> tags;
    private String visitedTime;

}
