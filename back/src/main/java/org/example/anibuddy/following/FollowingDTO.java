package org.example.anibuddy.following;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowingDTO {
	private String storeName, storeAddress, storeCategory;
	private List<String> storeImageList;
	private Integer storeId;
	
	public FollowingDTO() {
    }
	
    public FollowingDTO(String storeName, String storeAddress, String storeCategory, List<String> storeImageList, Integer storeId, Integer petId) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeCategory = storeCategory;
        this.storeImageList = storeImageList;
        this.storeId = storeId;
    }
}