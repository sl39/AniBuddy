package com.example.front;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
        public String getStoreName () {
            return storeName;
        }

        public void setStoreName (String storeName) {
            this.storeName = storeName;
        }

        public String getAddress () {
            return address;
        }

        public void setAddress (String address){
            this.address = address;
        }

        public String getStoreCategory () {
            return storeCategory;
        }

        public void setStoreCategory (String storeCategory) {
            this.storeCategory = storeCategory;
        }

        public List<StoreImage> getStoreImageList () {
            return storeImageList;
        }

        public void setStoreImageList (List<StoreImage> storeImageList) {
            this.storeImageList = storeImageList;
        }

        public Integer getId () {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;

        }
}