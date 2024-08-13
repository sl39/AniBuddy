package org.example.anibuddy.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreWithDistanceDTO {
    private int id;

    private String storeName;

    private String address;

    private String roadaddress;

    private String storeInfo;

    private String phoneNumber;

    private String openday;

    private double mapx;

    private double mapy;

    private double distance;
}
