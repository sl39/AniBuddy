package org.example.anibuddy.store.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StoreOwnerDetailResponseDto {
    private String storeName;
    private String storeAddress;
    private String storePhoneNumber;
    private String storeInfo;
    private List<String> openDay;
    private String openTime;
    private List<String> storeCategory;
    private List<String> images;

}
