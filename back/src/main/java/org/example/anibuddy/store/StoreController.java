package org.example.anibuddy.store;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.storeMongo.StoreMongoEntity;
import org.example.anibuddy.store.storeMongo.StoreMongoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreMongoService storeMongoService;

    @GetMapping("/store/all")
    public List<StoreMongoEntity> getAllStores() {
        return  storeMongoService.getAllStores();
    }

    @PostMapping("/store/test")
    public String setStore(@RequestBody StoreMongoEntity store){
        storeMongoService.setStores(store);
        return "success";

    }

}
