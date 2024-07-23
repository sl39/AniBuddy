package org.example.anibuddy.store.storeMongo;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.Store;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreMongoService {

    private final StoreMongoRepository storeMongoRepository;
    private final StoreRepository storeRepository;



    public List<StoreMongoEntity> getAllStores(){
        return storeMongoRepository.findAll();
    }

    public Integer setMongoStore(StoreMongoEntity store){
        Integer storeId =  setStore(store);
        store.set_id(storeId);
        storeMongoRepository.save(store);
        return store.get_id();
    }

    private Integer setStore(StoreMongoEntity store){
        Optional<Store> existingStore = storeRepository.findByStoreNameAndMapxAndMapy(
                store.getName(),
                store.getMapx(),
                store.getMapy()
        );

        if (existingStore.isPresent()) {
            throw new IllegalArgumentException("이미 존재함");
        }
        Store newStore = Store.fromStoreMongoEntity(store);
        storeRepository.save(newStore);
        return newStore.getStoreId();
    }
}
