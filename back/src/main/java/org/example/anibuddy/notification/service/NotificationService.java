package org.example.anibuddy.notification.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.model.Role;
import org.example.anibuddy.notification.dto.FcmTokenResponse;
import org.example.anibuddy.notification.dto.NotificationResponse;
import org.example.anibuddy.notification.model.FcmTokenEntity;
import org.example.anibuddy.notification.model.NotificationEntity;
import org.example.anibuddy.notification.repository.FcmTokenRepository;
import org.example.anibuddy.notification.repository.NotificationRepository;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.owner.OwnerRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    //알림 리스트 조회
    public List<NotificationResponse> getNotificationList(Role role, int id) {

        List<NotificationEntity> entities = new ArrayList<>();

        if(role.equals(Role.ROLE_USER)) {
            UserEntity userEntity = userRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));
            entities = notificationRepository.findAllByToUser(userEntity);
        }
        else if (role.equals(Role.ROLE_OWNER)) {
            OwnerEntity ownerEntity = ownerRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다."));
            entities = notificationRepository.findAllByToOwner(ownerEntity);
        }

        List<NotificationResponse> responses = new ArrayList<>();

        for(NotificationEntity entity : entities) {
            responses.add(new NotificationResponse(
                    entity.getNotification_type(),
                    entity.getNotified_at().toString(),
                    entity.getTitle(),
                    entity.getContent(),
                    entity.isRead()
            ));
        }
        System.out.println(responses.size());

        return responses;
    }

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
    public void pushChatNotification(int ReceiverId, Role ReceiverRole, String jsonMsg){
        //상대방의 FCM token 받아오기
        String otherFcmToken = null;

        if(ReceiverRole.equals(Role.ROLE_USER)) {
            UserEntity userEntity = userRepository
                    .findById(ReceiverId)
                    .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByUser(userEntity);
            if(optional.isPresent()){
                otherFcmToken = optional.get().getFcmToken();
            } else {
                throw new RuntimeException("상대방의 fcm 토큰이 존재하지 않습니다");
            }
        }
        else if (ReceiverRole.equals(Role.ROLE_OWNER)) {
            OwnerEntity ownerEntity = ownerRepository
                    .findById(ReceiverId)
                    .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByOwner(ownerEntity);
            if(optional.isPresent()){
                otherFcmToken = optional.get().getFcmToken();
            } else {
                //테스트 환경에서 Owner의 fcm Token을 발급받을 수 없어 주석처리
                //throw new RuntimeException("상대방의 fcm 토큰이 존재하지 않습니다");
            }
        }

        //FCM 서버에 푸시 메세지 요청 보내기
        if(otherFcmToken != null){
            Message message = Message.builder()
                    .putData("type", "chat")
                    .putData("data", jsonMsg)
                    .setToken(otherFcmToken)
                    .build();
            try {
                String response = FirebaseMessaging.getInstance().send(message);
                logger.info("Successfully sent chat push message: " + response);

            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        }
    }

    //예약 알림 fcm 서버에 요청
    public void pushReservationNotification(int ReceiverId, Role ReceiverRole, String jsonMsg){
        //상대방의 FCM token 받아오기
        String otherFcmToken = null;
        UserEntity userEntity = null;
        OwnerEntity ownerEntity = null;

        if(ReceiverRole.equals(Role.ROLE_USER)) {
            userEntity = userRepository
                    .findById(ReceiverId)
                    .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByUser(userEntity);
            if(optional.isPresent()){
                otherFcmToken = optional.get().getFcmToken();
            } else {
                throw new RuntimeException("상대방의 fcm 토큰이 존재하지 않습니다");
            }
        }
        else if (ReceiverRole.equals(Role.ROLE_OWNER)) {
            ownerEntity = ownerRepository
                    .findById(ReceiverId)
                    .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다."));

            Optional<FcmTokenEntity> optional = fcmTokenRepository.findByOwner(ownerEntity);
            if(optional.isPresent()){
                otherFcmToken = optional.get().getFcmToken();
            } else {
                //테스트 환경에서 Owner의 fcm Token을 발급받을 수 없어 주석처리
                throw new RuntimeException("상대방의 fcm 토큰이 존재하지 않습니다");
            }
        }

        //String -> JSONObject
        JSONObject obj = jsonToObjectParser(jsonMsg);
        Integer state = Integer.parseInt((String)obj.get("reservation_state"));
        String storeName = (String)obj.get("storeName");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일 a h시 m분", Locale.KOREAN);
        LocalDateTime localDateTime = LocalDateTime.parse((String)obj.get("reservation_date"));
        String date = localDateTime.format(formatter);

        //알림 메세지 생성
        String reservation_state = "";
        switch (state) {
            case 0: reservation_state = "접수"; break;
            case 1: reservation_state = "확정"; break;
            case 2: reservation_state = "취소"; break;
        }
        String title = "예약이 "+reservation_state+"되었습니다";
        String content = "["+storeName+"] "+date+" 예약이 "+reservation_state+"되었습니다.";

        //FCM 서버에 푸시 메세지 요청 보내기
        Message message = Message.builder()
                .putData("type", "reservation")
                .putData("title", title)
                .putData("content", content)
                .setToken(otherFcmToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Successfully sent reservation push message: " + response);

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        if(userEntity != null) {
            NotificationEntity entity = new NotificationEntity("RESERVATION", title, content, "ROLE_USER", userEntity, null);
            notificationRepository.save(entity);
        }
        else if (ownerEntity != null) {
            NotificationEntity entity = new NotificationEntity("RESERVATION", title, content, "ROLE_OWNER", null, ownerEntity);
            notificationRepository.save(entity);
        }
    }

    private JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return obj;
    }
}
