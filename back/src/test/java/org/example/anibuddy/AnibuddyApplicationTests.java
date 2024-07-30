package org.example.anibuddy;

import jakarta.transaction.Transactional;
import org.example.anibuddy.store.entity.StoreEntity;
//import org.example.anibuddy.store.entity.StoreSummary;
import org.example.anibuddy.store.repository.StoreRepository;
//import org.example.anibuddy.store.repository.StoreSummaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AnibuddyApplicationTests {

    private final StoreRepository storeRepository;
//    private final StoreSummaryRepository storeSummaryRepository;

    @Autowired
    AnibuddyApplicationTests(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
//        this.storeSummaryRepository = storeSummaryRepository;
    }


    @Test
    void contextLoads() {
    }

//    @Test
//    public void transToSummary(){
//        List<StoreEntity> storeEntityList =  storeRepository.findAll();
//        List<StoreSummary> storeSummaryList = new ArrayList<>();
//        for (StoreEntity storeEntity : storeEntityList) {
//            StoreSummary storeSummary = StoreSummary.builder()
//                    .id(storeEntity.getId())
//                    .mapy(storeEntity.getMapy())
//                    .mapx(storeEntity.getMapx())
//                    .storeInfo(storeEntity.getStoreInfo())
//                    .storeName(storeEntity.getStoreName())
//                    .address(storeEntity.getAddress())
//                    .roadaddress(storeEntity.getRoadaddress())
//                    .phoneNumber(storeEntity.getPhoneNumber())
//                    .openday(storeEntity.getOpenday())
//                    .storeEntity(storeEntity)
//                    .build();
//            storeSummaryList.add(storeSummary);
//        }
//
//        storeSummaryRepository.saveAll(storeSummaryList);
//    }
}
