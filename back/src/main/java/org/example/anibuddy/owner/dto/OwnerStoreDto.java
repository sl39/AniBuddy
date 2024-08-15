package org.example.anibuddy.owner.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OwnerStoreDto {
    private int id;
    private String storeName;
    private String storeAddress;
}
