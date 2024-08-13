package org.example.anibuddy.owner;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.owner.dto.OwnerStoreDto;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;

    public List<OwnerStoreDto> getStores() {
        List<OwnerStoreDto> stores = new ArrayList<OwnerStoreDto>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer ownerId = userDetails.getUserId();
        List<StoreEntity> storeEntities = storeRepository.findAllByOwnerEntity(ownerId);
        for(StoreEntity storeEntity : storeEntities) {
            OwnerStoreDto ownerStoreDto = OwnerStoreDto.builder()
                    .id(storeEntity.getId())
                    .storeAddress(storeEntity.getAddress())
                    .storeName(storeEntity.getStoreName())
                    .build();
            stores.add(ownerStoreDto);
        }
        return stores;

    }
}
