package org.example.anibuddy.owner;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.owner.dto.OwnerStoreDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping("/stores")
    public List<OwnerStoreDto> getStores() {
        List<OwnerStoreDto> storeDtos = ownerService.getStores();
        return storeDtos;
    }

}
