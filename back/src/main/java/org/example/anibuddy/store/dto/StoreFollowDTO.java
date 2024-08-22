package org.example.anibuddy.store.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.example.anibuddy.store.entity.StoreImage;


@Getter
@Setter
public class StoreFollowDTO {
	private String storeName, address, storeCategory;
	private List<StoreImage> storeImageList;
	private Integer id;
    
	public StoreFollowDTO() {
	}
	
	public StoreFollowDTO(String storeName, String address, String storeCategory, List<StoreImage> storeImageList, Integer id) {
		this.storeName = storeName;
		this.address = address;
		this.storeCategory = storeCategory;
		this.storeImageList = storeImageList;
		this.id = id;
	}
}
	