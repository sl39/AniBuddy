package org.example.anibuddy.notification.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.model.Role;
import org.example.anibuddy.notification.dto.FcmTokenResponse;
import org.example.anibuddy.notification.model.FcmTokenEntity;
import org.example.anibuddy.notification.repository.FcmTokenRepository;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.owner.OwnerRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // 토큰 등록
    @Transactional
    public FcmTokenResponse registerFcmToken(int id, Role role, String fcmTokenStr) {

        FcmTokenEntity fcmTokenEntity = null;
        String fcmToken = fcmTokenStr.replace("\"", "");

        // FcmToken 테이블에 정보 저장
        if(role.equals(Role.ROLE_USER)) {
            UserEntity userEntity = userRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByUser(userEntity);
            if(optional.isPresent()){
                fcmTokenEntity = optional.get();
                fcmTokenEntity.setFcmToken(fcmToken);
                fcmTokenRepository.save(fcmTokenEntity);
            } else {
                fcmTokenEntity = fcmTokenRepository.save(
                        FcmTokenEntity.builder().userEntity(userEntity).role("USER").fcmToken(fcmToken).build());
            }
        }
        else if (role.equals(Role.ROLE_OWNER)) {
            OwnerEntity ownerEntity = ownerRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByOwner(ownerEntity);
            if(optional.isPresent()){
                fcmTokenEntity = optional.get();
                fcmTokenEntity.setFcmToken(fcmToken);
                fcmTokenRepository.save(fcmTokenEntity);
            } else {
                fcmTokenEntity = fcmTokenRepository.save(
                        FcmTokenEntity.builder().ownerEntity(ownerEntity).role("OWNER").fcmToken(fcmToken).build());
            }
        }

        logger.info("id:" + fcmTokenEntity.getId() + " role:" + fcmTokenEntity.getRole() + " token:" + fcmTokenEntity.getFcmToken());

        // Response 생성해서 리턴
        return new FcmTokenResponse(id, role.name(), fcmToken);
    }

    //채팅 알림 fcm 서버에 요청
    public void pushChatNotification(int id, Role role, String name, String jsonMsg){
        //상대방의 FCM token 받아오기
        String otherFcmToken = null;

        if(role.equals(Role.ROLE_USER)) {
            UserEntity userEntity = userRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByUser(userEntity);
            if(optional.isPresent()){
                otherFcmToken = optional.get().getFcmToken();
            } else {
                throw new RuntimeException("상대방의 fcm 토큰이 존재하지 않습니다");
            }
        }
        else if (role.equals(Role.ROLE_OWNER)) {
            OwnerEntity ownerEntity = ownerRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByOwner(ownerEntity);
            if(optional.isPresent()){
                otherFcmToken = optional.get().getFcmToken();
            } else {
                throw new RuntimeException("상대방의 fcm 토큰이 존재하지 않습니다");
            }
        }

        //FCM 서버에 푸시 메세지 요청 보내기
        Message message = Message.builder()
                .putData("type", "chat")
                .putData("data", jsonMsg)
                .setToken(otherFcmToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    //예약 알림 fcm 서버에 요청
    public void pushReservationNotification(){

    }
}
