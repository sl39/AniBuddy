package org.example.anibuddy.store.storeMongo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewMongoDto {
    private int userId;
    private String nickName;
    private String review;
    private List<String> reviewImageList;
    private List<String> tags;
    private String visitedTime;

}
