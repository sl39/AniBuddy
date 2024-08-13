package com.example.front;

import java.util.List;

public class StoreImage {
    private String imageUrl;

    private Integer id;

    public StoreImage() {
    }

    public StoreImage(String imageUrl, Integer id) {
        this.imageUrl = imageUrl;
        this.id = id;

    }
    public String getImageUrl () {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl) {this.imageUrl = imageUrl; }

    public Integer getId () {
        return id;
    }

    public void setId (Integer id){
        this.id = id;
    }
}
