package org.example.anibuddy.store.service;

import jakarta.transaction.Transactional;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.dto.StoreCreateDto;
import org.example.anibuddy.store.dto.StoreWithDistanceDTO;
import org.example.anibuddy.store.entity.StoreCategory;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.entity.StoreImage;
import org.example.anibuddy.store.repository.StoreCategoryRepository;
import org.example.anibuddy.store.repository.StoreImageRepository;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.store.repository.StoreSummaryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreSummaryRepository storeSummaryRepository;
    private final StoreCategoryRepository storeCategoryRepository;

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
    @Transactional
    public String createStoreAll(List<StoreCreateDto> storeCreateDtoList){
        for(StoreCreateDto storeCreateDto: storeCreateDtoList){

            List<StoreCategory> storeCategories = new ArrayList<>();
            for(String cate: storeCreateDto.getCategory()){
                int id;
                if (cate.equals("beauty")){
                    id = 1;
                } else if (cate.equals("hospital")){
                    id = 2;
                } else if (cate.equals("training")) {
                    id = 3;
                } else {
                    id = 4;
                    cate = "etc";
                }
                StoreCategory category = new StoreCategory().builder()
                        .id(id)
                        .category(cate)
                        .build();
                storeCategories.add(category);
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
                    .storeCategoryList(storeCategories)
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



        }


        return "success";
    }




    public Optional<StoreEntity> getStore(String storeName, String address){
        Optional<StoreEntity> storeEntity = storeRepository.findByStoreNameAndAddress(storeName, address);
        return storeEntity;
    }






    public List<StoreWithDistanceDTO> getMainStore(double mapx, double mapy) {
        List<Map<String, Object>> storeWithDistanceDTOList = storeSummaryRepository.findStoresWithinDistance(mapx,mapy);
        System.out.println(storeWithDistanceDTOList.size());
        List<StoreWithDistanceDTO> dtos = new ArrayList<>();
        for(Map<String,Object> result : storeWithDistanceDTOList){
            StoreWithDistanceDTO storeWithDistanceDTO = StoreWithDistanceDTO
                    .builder()
                    .id((Integer) result.get("id"))
                    .storeName((String) result.get("storeName"))
                    .address((String) result.get("address"))
                    .roadaddress((String) result.get("roadaddress"))
                    .storeInfo((String) result.get("storeInfo"))
                    .phoneNumber((String) result.get("phoneNumber"))
                    .openday((String) result.get("openday"))
                    .mapx((Double) result.get("mapx"))
                    .mapy((Double) result.get("mapy"))
                    .distance((Double) result.get("distance"))
                    .build();
            dtos.add(storeWithDistanceDTO);
        }
        return dtos;
    }
}
