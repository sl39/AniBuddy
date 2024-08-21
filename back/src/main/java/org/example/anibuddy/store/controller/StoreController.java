package org.example.anibuddy.store.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.dto.*;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.service.StoreService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/all")
    public List<StoreEntity> getStoreAll(){
        return storeService.findAll();
    }

    @PostMapping("/create")
    public Map<String,String> createStore(@RequestBody StoreCreateDto storeCreateDto){
        ResponseEntity response = storeService.createStore(storeCreateDto);

        return Map.of("message", "Store created");
    }

    @GetMapping("/main")
    public List<MainReviewSimpleResponseDto> getMainStore(MainReviewSimpleRequestDto resqeust){
        System.out.println(resqeust.getMapx());
        List<MainReviewSimpleResponseDto> storeEntityList = storeService.getMainStore(resqeust.getMapx(), resqeust.getMapy(), resqeust.getCategory());
        System.out.println(storeEntityList.size());
        return storeEntityList;
    }

    @PostMapping("/create/all")
    public String createStoreAll(@RequestBody List<StoreCreateDto> storeCreateDtoList){
        storeService.createStoreAll(storeCreateDtoList);
        return "success";
    }

    @PostMapping("/category")
    public String category(@RequestBody List<StoreCreateDto> storeCreateDtoList){
        storeService.setCategory(storeCreateDtoList);
        return "success";
    }

    @GetMapping("/search/location")
    public List<StoreSearchLocationCategoryResponse> serachLocationCategory(StoreSearchLocationCategoryRequestDto reqeust){
        return storeService.serachLocationCategory(reqeust.getDistrict(),reqeust.getCategory(),reqeust.getMapx(), reqeust.getMapy(), reqeust.getName());
    }

    @GetMapping("/{storeId}")
    public StoreDetailDTO getStoreById(@PathVariable(value = "storeId") Integer storeId){
        return storeService.getStoreById(storeId);
    }

    @GetMapping("/owner")
    public StoreOwnerDetailResponseDto getStoreOwnerById(@RequestParam("storeId") Integer storeId){
        System.out.println("들어옴?");
        return storeService.getStoreOwnerById(storeId);
    }

}
