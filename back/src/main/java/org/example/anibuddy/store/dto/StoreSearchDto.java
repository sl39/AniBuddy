package org.example.anibuddy.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.anibuddy.store.entity.StoreEntity;

@Getter
@Setter
@Builder
public class StoreSearchDto {

    private int id;

    private String storeName;

    private String address;

    private String roadaddress;

    private String storeInfo;

    private String phoneNumber;

    private String openday;

    private double mapx;

    private double mapy;
}
