package org.example.anibuddy.store.service;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.dto.StoreCreateDto;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.entity.StoreImage;
import org.example.anibuddy.store.repository.StoreImageRepository;
import org.example.anibuddy.store.repository.StoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    public List<StoreEntity> findAll(){
        return storeRepository.findAll();
    }

    public ResponseEntity<?> createStore(StoreCreateDto storeCreateDto){
        Optional<StoreEntity> storeEntity = storeRepository.findByStoreNameAndAddress(storeCreateDto.getName(), storeCreateDto.getAddress());

        if(storeEntity.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        StoreEntity newStoreEntity = new StoreEntity().builder()
                .mapx(storeCreateDto.getMapx())
                .mapy(storeCreateDto.getMapy())
                .address(storeCreateDto.getAddress())
                .roadaddress(storeCreateDto.getRoadAddress())
                .storeName(storeCreateDto.getName())
                .storeInfo(storeCreateDto.getInfo())
                .openday(storeCreateDto.getOpenDay())
                .phoneNumber(storeCreateDto.getPhone_number())
                .build();


        storeRepository.save(newStoreEntity);

        List<StoreImage> storeImagesList = new ArrayList<>();
        for(String imageUrl: storeCreateDto.getStoreImageList()){
            StoreImage storeImage = new StoreImage().builder()
                    .imageUrl(imageUrl)
                    .storeEntity(newStoreEntity)
                    .build();
            storeImagesList.add(storeImage);
        }

        storeImageRepository.saveAll(storeImagesList);
        return new ResponseEntity<>(newStoreEntity.getStoreName(), HttpStatus.OK);
    }

    public Optional<StoreEntity> getStore(String storeName, String address){
        Optional<StoreEntity> storeEntity = storeRepository.findByStoreNameAndAddress(storeName, address);
        return storeEntity;
    }
}
