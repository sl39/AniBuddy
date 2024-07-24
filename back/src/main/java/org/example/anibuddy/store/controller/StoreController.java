package org.example.anibuddy.store.controller;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.dto.StoreCreateDto;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
