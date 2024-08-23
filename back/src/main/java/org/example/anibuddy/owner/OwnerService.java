package org.example.anibuddy.owner;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.owner.dto.OwnerStoreDto;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public List<OwnerStoreDto> getStores() {
        List<OwnerStoreDto> stores = new ArrayList<OwnerStoreDto>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Optional<OwnerEntity> owner = ownerRepository.findByUserEntity(userEntity.get());
        if(owner.isEmpty()){
            System.out.println(6);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
        }


        List<StoreEntity> storeEntities = storeRepository.findAllByOwnerEntity(owner.get().getId());
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
