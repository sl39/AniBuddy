package org.example.anibuddy.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.anibuddy.chat.dto.ChatRoomRequest;
import org.example.anibuddy.chat.dto.ChatRoomResponse;
import org.example.anibuddy.chat.model.ChatRoomEntity;
import org.example.anibuddy.chat.model.Role;
import org.example.anibuddy.chat.repository.ChatRoomRepository;
import org.example.anibuddy.owner.OwnerEntity;
import org.example.anibuddy.owner.OwnerRepository;
import org.example.anibuddy.user.UserEntity;
import org.example.anibuddy.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;

    @Transactional
    public ChatRoomResponse makeChatRoom(int userId, ChatRoomRequest chatRoomRequest) {

        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));
        OwnerEntity owner = ownerRepository
                .findById(chatRoomRequest.getOwnerId())
                .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다"));
        Optional<ChatRoomEntity> optionalChatRoom = chatRoomRepository.findByUserAndOwner(user, owner);

        ChatRoomEntity chatRoom = optionalChatRoom.orElseGet(() ->
                chatRoomRepository.save(ChatRoomEntity.builder().user(user).owner(owner).build()));

        //TODO: Auth로 판변해서 상대방 정보만 넘겨주기
        return new ChatRoomResponse(1, "test name", "test Url");
    }

    public List<ChatRoomResponse> getChatRoomList(Role role, int id) {

        List<ChatRoomEntity> entities = new ArrayList<>();

        if(role.equals(Role.USER)) {
            UserEntity user = userRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("user 정보가 조회되지 않습니다."));
            entities = chatRoomRepository.findAllByUser(user);
        }
        else if (role.equals(Role.OWNER)) {
            OwnerEntity owner = ownerRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("owner 정보가 조회되지 않습니다."));
            entities = chatRoomRepository.findAllByOwner(owner);
        }

        List<ChatRoomResponse> responses = new ArrayList<>();
        String otherName;
        for(ChatRoomEntity chatRoom : entities) {
            if(role == Role.USER) otherName = chatRoom.getOwner().getName();
            else otherName = chatRoom.getUser().getUserName();
            responses.add(new ChatRoomResponse(chatRoom.getId(), otherName, "https://avc.com"));
        }

        return responses;
    }
}



