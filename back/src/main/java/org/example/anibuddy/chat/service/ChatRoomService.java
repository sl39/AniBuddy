package org.example.anibuddy.chat.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

        return new ChatRoomResponse(chatRoom);
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
        for(ChatRoomEntity chatRoom : entities) {
            responses.add(new ChatRoomResponse(chatRoom));
        }
        return responses;
    }
}



