package org.example.anibuddy.store.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.dto.MainReviewSimpleResponseDto;
import org.example.anibuddy.store.dto.StoreCreateDto;
import org.example.anibuddy.store.dto.StoreSearchLocationCategoryResponse;
import org.example.anibuddy.store.dto.StoreWithDistanceDTO;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<?> createStore(@RequestBody StoreCreateDto storeCreateDto){
        ResponseEntity  response = storeService.createStore(storeCreateDto);
        return response;
    }

    @GetMapping("/main/{mapx}/{mapy}/{category}")
    public List<MainReviewSimpleResponseDto> getMainStore(@PathVariable(value = "mapx") double mapx, @PathVariable(value = "mapy") double mapy, @PathVariable(value = "category") String category){
        System.out.println(mapx + mapy + category);
        List<MainReviewSimpleResponseDto> storeEntityList = storeService.getMainStore(mapx,mapy,category);
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

    @GetMapping("/search/location/{district}/{category}/{mapx}/{mapy}")
    public List<StoreSearchLocationCategoryResponse> serachLocationCategory(@PathVariable(value = "district") String district, @PathVariable(value = "category") String category, @PathVariable(value = "mapx") double mapx,@PathVariable(value = "mapy") double mapy){
        return storeService.serachLocationCategory(district,category,mapx,mapy);

    }

}
