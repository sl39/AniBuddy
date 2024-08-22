package org.example.anibuddy.store.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.anibuddy.store.entity.StoreImage;

@Getter
@Setter
@Builder
public class StoreDetailDTO {
	private String storeName, address, roadaddress, storeInfo, phoneNumber, openday, district;
	private List<String> storeImageList;
	private double mapx, mapy;
	private Integer id;
	

    public StoreDetailDTO(String storeName, String address, String roadaddress, String storeInfo, String phoneNumber, String openday, String district, List<String> storeImageList, double mapx, double mapy, Integer id) {
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