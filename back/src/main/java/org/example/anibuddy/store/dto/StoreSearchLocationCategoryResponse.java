package org.example.anibuddy.store.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StoreSearchLocationCategoryResponse {
    private Integer id;
    private String storeName;
    private String category;
    private double distance;
    private Integer reviewCount;
    private List<String> storeImage;

}
