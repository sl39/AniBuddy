package org.example.anibuddy.reservation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.anibuddy.Review.dto.ReviewDetailResponseDto;
import org.example.anibuddy.chat.model.Role;
import org.example.anibuddy.global.CustomUserDetails;
import org.example.anibuddy.notification.service.NotificationService;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.owner.OwnerRepository;
import org.example.anibuddy.reservation.dto.*;
import org.example.anibuddy.reservation.entity.ReservationEntity;
import org.example.anibuddy.reservation.repository.ReservationRepository;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final NotificationService notificationService;

    public Integer addReservation(ReservationCreateRequestDto reservation) throws Exception {
        Integer storeId = reservation.getStoreId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
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
                .state(0)
                .build();

        ReservationEntity res = reservationRepository.save(reservationEntity);

        // 예약 접수 fcm 알림 발송
        JSONObject obj = new JSONObject();
        obj.put("storeName", storeEntity.getStoreName());
        obj.put("reservation_date", reservationEntity.getReservationDate().toString());
        obj.put("reservation_state", "0");

        notificationService.pushReservationNotification(user.get().getId(), Role.ROLE_USER, obj.toJSONString());
        //현재 데이터에는 Owner가 없는 Store가 대부분이라 getOwnerEntity()에서 에러가 나 예약 실패가 되기 때문에 주석 처리
        //notificationService.pushReservationNotification(storeEntity.getOwnerEntity().getId(), Role.ROLE_OWNER, obj.toJSONString());

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
                .reservationId(reservationId)
                .storePhoneNumber(storeEntity.getPhoneNumber())
                .storeId(storeEntity.getId())
                .state(reservation.getState())
                .build();

        return responseDto;

    }

    @Transactional
    public Integer updateReservation(ReservationUpdateRequestDto reservation) throws Exception {
        Integer storeId = reservation.getStoreId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        System.out.println(id);
        Optional<StoreEntity> store = storeRepository.findById(storeId);
        Optional<UserEntity> user = userRepository.findById(id);
        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(reservation.getReservationId());
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        if (store.isEmpty()) {
            throw new Exception("스토어가 존재하지 않습니다");
        }
        ReservationEntity resEntity = reservationEntity.get();
        resEntity.setReservationDate(LocalDateTime.parse(reservation.getReservationTime()));
        resEntity.setInfo(reservation.getInfo());
        reservationRepository.save(resEntity);

        return resEntity.getId();
    }

    public void deleteReservationDetail(Integer reservationId) throws Exception {
        Optional<ReservationEntity> reservationEntity =  reservationRepository.findById(reservationId);
        if(reservationEntity.isEmpty()){
            throw new Exception("예약이 존재하지 않습니다");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        reservationRepository.delete(reservationEntity.get());
    }

    public List<ReservationGetAllResponseDto> getAllReservation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        List<ReservationGetAllResponseDto> responseDtos = new ArrayList<>();
        List<ReservationEntity> reservationEntities = reservationRepository.findAllByUserEntity(user.get());
        for(ReservationEntity reservationEntity : reservationEntities){
            ReservationGetAllResponseDto res = ReservationGetAllResponseDto.builder()
                    .info(reservationEntity.getInfo())
                    .id(reservationEntity.getId())
                    .reservationTime(reservationEntity.getReservationDate().toString())
                    .storeId(reservationEntity.getStoreEntity().getId())
                    .storeName(reservationEntity.getStoreEntity().getStoreName())
                    .storeAddress(reservationEntity.getStoreEntity().getAddress())
                    .state(reservationEntity.getState())
                    .build();
            responseDtos.add(res);
        }
        return responseDtos;
    }

    public void updateState(ReservationStateRequestDto reservation) throws Exception {
        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(reservation.getReservationId());
        if(reservationEntity.isEmpty()){
            throw new Exception("예약이 존재하지 않습니다");
        }
        ReservationEntity res = reservationEntity.get();
        res.setState(reservation.getState());
        reservationRepository.save(res);

        // 예약 확정/취소 fcm 알림 발송 - 현재 데이터에는 Owner가 없는 Store가 대부분이라 getOwnerEntity()에서 에러 날 수 있음!
        JSONObject obj = new JSONObject();
        obj.put("storeName", res.getStoreEntity().getStoreName());
        obj.put("reservation_date", res.getReservationDate().toString());
        obj.put("reservation_state", res.getState().toString());

        notificationService.pushReservationNotification(res.getUserEntity().getId(), Role.ROLE_USER, obj.toJSONString());
        //현재 데이터에는 Owner가 없는 Store가 대부분이라 getOwnerEntity()에서 에러가 나 예약 실패가 되기 때문에 주석 처리
        //notificationService.pushReservationNotification(res.getStoreEntity().getOwnerEntity().getId(), Role.ROLE_OWNER, obj.toJSONString());
    }

    public List<ReservationGetAllResponseDto> getAllOwnerReservation() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        Optional<OwnerEntity> owner = ownerRepository.findByUserEntity(user.get());
        if(owner.isEmpty()){
            throw new Exception("오너가 존재하지 않습니다");
        }
        List<ReservationGetAllResponseDto> responseDtos = new ArrayList<>();

        List<ReservationEntity> reservationEntities = reservationRepository.findByOwner(owner.get().getId());
        for(ReservationEntity reservationEntity : reservationEntities){
            ReservationGetAllResponseDto res = ReservationGetAllResponseDto.builder()
                    .info(reservationEntity.getInfo())
                    .id(reservationEntity.getId())
                    .reservationTime(reservationEntity.getReservationDate().toString())
                    .storeId(reservationEntity.getStoreEntity().getId())
                    .storeName(reservationEntity.getStoreEntity().getStoreName())
                    .storeAddress(reservationEntity.getStoreEntity().getAddress())
                    .state(reservationEntity.getState())
                    .build();
            responseDtos.add(res);
        }
        return responseDtos;
    }

    public ReservationGetResponseDto getOwnerReservationDetail(Integer reservationId) throws Exception {
        Optional<ReservationEntity> reservationEntity =  reservationRepository.findById(reservationId);
        if(reservationEntity.isEmpty()){
            throw new Exception("예약이 존재하지 않습니다");
        }
        ReservationEntity reservation = reservationEntity.get();
        StoreEntity storeEntity = reservation.getStoreEntity();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer id = userDetails.getUserId();
        if(id != storeEntity.getOwnerEntity().getId()){
            throw new Exception("유저가 일치 하지 않습니다");
        }
        ReservationGetResponseDto responseDto = ReservationGetResponseDto.builder()
                .info(reservation.getInfo())
                .reservationDateTime(reservation.getReservationDate().toString())
                .storeLocation(storeEntity.getAddress())
                .storeName(storeEntity.getStoreName())
                .reservationId(reservationId)
                .storePhoneNumber(storeEntity.getPhoneNumber())
                .storeId(storeEntity.getId())
                .state(reservation.getState())
                .build();

        return responseDto;

    }
}
