package org.example.anibuddy.store;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.store.storeMongo.StoreRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public Store getStore(String storeName) {
        Optional<Store> store = Optional.ofNullable(storeRepository.findByStoreName(storeName).orElseThrow(() -> new UsernameNotFoundException("email not found")));

        return store.get();
    }

}
