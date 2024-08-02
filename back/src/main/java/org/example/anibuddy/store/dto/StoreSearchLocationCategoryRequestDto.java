package org.example.anibuddy.store.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreSearchLocationCategoryRequestDto {
    private String category;
    private List<String> district;
    private String name;
    private double mapx;
    private double mapy;

}
