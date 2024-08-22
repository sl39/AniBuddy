package org.example.anibuddy.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.anibuddy.Review.dto.ReviewDetailResponseDto;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.reservation.dto.ReservationCreateRequestDto;
import org.example.anibuddy.reservation.dto.ReservationGetResponseDto;
import org.example.anibuddy.reservation.entity.ReservationEntity;
import org.example.anibuddy.reservation.repository.ReservationRepository;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public Integer addReservation(ReservationCreateRequestDto reservation) throws Exception {
        Integer storeId = reservation.getStoreId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        System.out.println(id);
        Optional<StoreEntity> store = storeRepository.findById(storeId);
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        if (store.isEmpty()) {
            throw new Exception("스토어가 존재하지 않습니다");
        }
        StoreEntity storeEntity = store.get();
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .info(reservation.getInfo())
                .reservationDate(LocalDateTime.parse(reservation.getReservationTime()))
                .storeEntity(storeEntity)
                .userEntity(user.get())
                .build();

        ReservationEntity res = reservationRepository.save(reservationEntity);
        return res.getId();
    }

    public ReservationGetResponseDto getReservationDetail(Integer reservationId) throws Exception {
        Optional<ReservationEntity> reservationEntity =  reservationRepository.findById(reservationId);
        if(reservationEntity.isEmpty()){
            throw new Exception("예약이 존재하지 않습니다");
        }
        ReservationEntity reservation = reservationEntity.get();
        StoreEntity storeEntity = reservation.getStoreEntity();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        System.out.println("여기는 겟 하는 곳 :" + id);
        if(id != reservation.getUserEntity().getId()){
            throw new Exception("유저가 일치 하지 않습니다");
        }
        ReservationGetResponseDto responseDto = ReservationGetResponseDto.builder()
                .info(reservation.getInfo())
                .reservationDateTime(reservation.getReservationDate().toString())
                .storeLocation(storeEntity.getAddress())
                .storeName(storeEntity.getStoreName())
                .build();

        return responseDto;

    }
}
