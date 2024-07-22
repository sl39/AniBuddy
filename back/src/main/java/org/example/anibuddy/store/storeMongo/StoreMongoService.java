package org.example.anibuddy.store.storeMongo;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreMongoService {

    private final StoreMongoRepository storeMongoRepository;

    public List<StoreMongoEntity> getAllStores(){
        return storeMongoRepository.findAll();
    }

    public String setStores(StoreMongoEntity store){
        System.out.println(store.toString());

        storeMongoRepository.save(store);
        return "굿";
    }
}
