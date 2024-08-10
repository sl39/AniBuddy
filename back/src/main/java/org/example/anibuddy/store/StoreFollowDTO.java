package org.example.anibuddy.store;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StoreFollowDTO {
	private String storeName, address, roadaddress;
	private List<StoreImage> storeImageList;
	private Integer id;
    
	public StoreFollowDTO() {
	}
	
	public StoreFollowDTO(String storeName, String address, String roadaddress, List<StoreImage> storeImageList, Integer id) {
		this.storeName = storeName;
		this.address = address;
		this.roadaddress = roadaddress;
		this.storeImageList = storeImageList;
		this.id = id;
	}
}
// roadaddress를 category로 가정하고 api 작성. 이후 category 해결되면 roadaddresss 수정.
	