package org.example.anibuddy.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreCreateDto {

    private String name;

    private String address;

    private String roadAddress;

    private String info;

    private String phone_number;

    private String openDay;

    private double mapx;

    private double mapy;

    private String district;

    private List<String>  storeImageList;

    private List<String> category;

}
