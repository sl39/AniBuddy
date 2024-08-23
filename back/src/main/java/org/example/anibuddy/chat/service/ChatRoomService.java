package org.example.anibuddy.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.anibuddy.chat.dto.ChatRoomRequest;
import org.example.anibuddy.chat.dto.ChatRoomResponse;
import org.example.anibuddy.chat.model.ChatRoomEntity;
import org.example.anibuddy.chat.model.Role;
import org.example.anibuddy.chat.repository.ChatRoomRepository;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.owner.OwnerRepository;
import org.example.anibuddy.store.entity.StoreEntity;
import org.example.anibuddy.store.repository.StoreRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ChatRoomResponse makeChatRoom(int userId, ChatRoomRequest chatRoomRequest) {

        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));
        StoreEntity store = storeRepository
                .findById(chatRoomRequest.getStoreId())
                .orElseThrow(() -> new RuntimeException("store 정보가 조회되지 않습니다."));

        OwnerEntity owner = store.getOwnerEntity();
        if(owner == null) throw new RuntimeException("owner 정보가 조회되지 않습니다.");

        // 이미 같은 구성원의 채팅방이 존재하는지 확인
        Optional<ChatRoomEntity> optionalChatRoom = chatRoomRepository.findByUserAndOwner(user, owner);

        // 있다면 조회한 정보가 저장되고, 없다면 새로 생성된 정보가 저장됨
        ChatRoomEntity chatRoom = optionalChatRoom.orElseGet(() ->
                chatRoomRepository.save(ChatRoomEntity.builder().user(user).owner(owner).build()));

        // Owner 정보 넘겨주기
        return new ChatRoomResponse(
                chatRoom.getId(),

                chatRoom.getUser().getId(),
                Role.ROLE_USER.name(),
                chatRoom.getUser().getUserName(),
                chatRoom.getUser().getImageUrl(),

                chatRoom.getOwner().getId(),
                Role.ROLE_OWNER.name(),
                chatRoom.getOwner().getName(),
                "https://avc.com"
        );
    }

    public List<ChatRoomResponse> getChatRoomList(Role role, int id) {

        List<ChatRoomEntity> entities = new ArrayList<>();

        if(role.equals(Role.ROLE_USER)) {
            UserEntity user = userRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));
            entities = chatRoomRepository.findAllByUser(user);
        }
        else if (role.equals(Role.ROLE_OWNER)) {
            OwnerEntity owner = ownerRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다."));
            entities = chatRoomRepository.findAllByOwner(owner);
        }

        List<ChatRoomResponse> responses = new ArrayList<>();

        int myId;
        String myRole;
        String myName;
        String myImageUrl;

        int otherId;
        String otherRole;
        String otherName;
        String otherImageUrl;

        for(ChatRoomEntity chatRoom : entities) {
            if(role == Role.ROLE_USER) {
                myName = chatRoom.getUser().getUserName();
                myId = chatRoom.getUser().getId();
                myRole = Role.ROLE_USER.name();
                myImageUrl = "https://avc.com";
                otherName = chatRoom.getOwner().getName();
                otherImageUrl = "https://avc.com";
                otherId = chatRoom.getOwner().getId();
                otherRole = Role.ROLE_OWNER.name();
            }
            else {
                myName = chatRoom.getOwner().getName();
                myId = chatRoom.getOwner().getId();
                myRole = Role.ROLE_OWNER.name();
                myImageUrl = "https://avc.com";
                otherName = chatRoom.getUser().getUserName();
                otherImageUrl = "https://avc.com";
                otherId = chatRoom.getUser().getId();
                otherRole = Role.ROLE_USER.name();
            }
            responses.add(new ChatRoomResponse(chatRoom.getId(), myId, myRole, myName, myImageUrl, otherId, otherRole, otherName, otherImageUrl));
        }
        System.out.println(responses.size());

        return responses;
    }
}



