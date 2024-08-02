package org.example.anibuddy.store.service;

import jakarta.transaction.Transactional;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.dto.MainReviewSimpleResponseDto;
import org.example.anibuddy.store.dto.StoreCreateDto;
import org.example.anibuddy.store.dto.StoreSearchLocationCategoryResponse;
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

import java.sql.Date;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreSummaryRepository storeSummaryRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    public List<StoreEntity> findAll(){
        return storeRepository.findTop10ByOrderByIdDesc();
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
            storeImageRepository.saveAll(storeImagesList);
        }
        return "success";
    }


    public Optional<StoreEntity> getStore(String storeName, String address){
        Optional<StoreEntity> storeEntity = storeRepository.findByStoreNameAndAddress(storeName, address);
        return storeEntity;
    }

    public List<MainReviewSimpleResponseDto> getMainStore(double mapx, double mapy, String category) {
        int categoryId = 0;
        if (category.equals("beauty")){
            categoryId = 1;
        } else if (category.equals("hospital")){
            categoryId = 2;
        } else if (category.equals("training")) {
            categoryId = 3;
        }

        List<Map<String, Object>> mainReviewSimpleResponseDto = storeRepository.findStoresWithinDistance(mapx,mapy, categoryId);
        List<MainReviewSimpleResponseDto> dtos = new ArrayList<>();
        for(Map<String,Object> result : mainReviewSimpleResponseDto){
            LocalDate createdDate = ((Date) result.get("createdDate")).toLocalDate();
            LocalDate modifiedDate = result.get("modifiedDate") != null ? ((Date) result.get("modifiedDate")).toLocalDate() : null;
            MainReviewSimpleResponseDto storeWithDistanceDTO = MainReviewSimpleResponseDto
                    .builder()
                    .storeId((Integer) result.get("storeId"))
                    .distance((Double) result.get("distance"))
                    .createdDate(createdDate)
                    .review((String) result.get("review"))
                    .modifiedDate(modifiedDate)
                    .imageUrl((String) result.get("imageUrl"))
                    .category(category)
                    .build();
            dtos.add(storeWithDistanceDTO);
        }
        return dtos;
    }




    public void setCategory(List<StoreCreateDto> storeCreateDtoList) {
        List<StoreEntity> storeEntities = new ArrayList<>();
        for(StoreCreateDto storeCreateDto: storeCreateDtoList){

            Optional<StoreEntity> storeEntity = storeRepository.findByStoreNameAndAddress(storeCreateDto.getName(), storeCreateDto.getAddress());
            if (storeEntity.isPresent()) {
                StoreEntity storeEntity1 = storeEntity.get();
                List<StoreCategory> storeCategories = new ArrayList<>();
                for(String cate: storeCreateDto.getCategory()){
                    int id = 0;
                    if (cate.equals("beauty")){
                        id = 1;
                    } else if (cate.equals("hospital")){
                        id = 2;
                    } else if (cate.equals("training")) {
                        id = 3;
                    }
                    StoreCategory category = new StoreCategory().builder()
                            .id(id)
                            .category(cate)
                            .build();
                    storeCategories.add(category);
                }
                storeEntity1.setStoreCategoryList(storeCategories);
                storeEntity1.setDistrict(storeCreateDto.getDistrict());
                storeEntities.add(storeEntity1);
            }
        }
        storeRepository.saveAll(storeEntities);
    }

    public List<StoreSearchLocationCategoryResponse> serachLocationCategory(List<String> district, String category, double mapx, double mapy,String name) {
        if(district == null){
            String[] d = {"북구", "남구", "동래구", "금정구", "사상구", "부산진구", "강서구", "해운대구", "동구", "영도구", "연제구", "사하구", "중구", "수영구", "기장군", "서구"};
            district = Arrays.asList(d);
        }
        List<StoreSearchLocationCategoryResponse> storeSearchLocationCategoryResponses = new ArrayList<>();
        int categoryId = 0;
        if (category.equals("beauty")){
            categoryId = 1;
        } else if (category.equals("hospital")){
            categoryId = 2;
        } else if (category.equals("training")) {
            categoryId = 3;
        }
        List<Map<String, Object>> storeList = storeRepository.findStoresByCategoryAndDistrictWithReview(district, categoryId, mapx ,mapy,name);
        for (Map<String, Object> result : storeList) {
            Integer reviewCount = Integer.parseInt(String.valueOf(result.get("reviewCount")));
            Integer storeId = Integer.parseInt(String.valueOf(result.get("storeId")));
            List<String> storeImages = getStoreImages(storeId);
            StoreSearchLocationCategoryResponse storeSearchLocationCategoryResponse = StoreSearchLocationCategoryResponse.builder()
                    .reviewCount(reviewCount)
                    .category(category)
                    .storeName((String) result.get("storeName"))
                    .distance((double) result.get("distance"))
                    .id(storeId)
                    .storeImage(storeImages)
                    .build();
            storeSearchLocationCategoryResponses.add(storeSearchLocationCategoryResponse);
        }
        return storeSearchLocationCategoryResponses;
    }

    public List<String> getStoreImages(Integer storeId) {
        List<String> storeImg = new ArrayList<>();
        Optional<List<String>> storeImages = storeImageRepository.findAllByStoreId(storeId);
        if (storeImages.isPresent()) {
            return storeImages.get();
        }
        return storeImg;
    }
}
