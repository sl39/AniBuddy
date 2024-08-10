package org.example.anibuddy.store;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDetailDTO {
	private String storeName, address, roadaddress, storeInfo, phoneNumber, openday, district;
	private List<StoreImage> storeImageList;
	private double mapx, mapy;
	private Integer id;
	
	public StoreDetailDTO() {
    }
    
    public StoreDetailDTO(String storeName, String address, String roadaddress, String storeInfo, String phoneNumber, String openday, String district, List<StoreImage> storeImageList, double mapx, double mapy, Integer id) {
    	this.storeName = storeName;
    	this.address = address;
    	this.roadaddress = roadaddress;
    	this.storeInfo = storeInfo;
    	this.phoneNumber = phoneNumber;
    	this.openday = openday;
    	this.district = district;
    	this.storeImageList = storeImageList;
    	this.mapx = mapx;
    	this.mapy = mapy;
    	this.id = id;
    }
}