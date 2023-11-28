package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.missionchatroom.UserMissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.repository.UserMissionChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserMissionChatRoomReader {

    private final UserMissionChatRoomRepository userMissionChatRoomRepository;

    public List<UserMissionChatRoom> findByChatRoomId(Long chatRoomId) {
        var userMissionChatRooms = userMissionChatRoomRepository.findByMissionChatRoom_Id(chatRoomId);

        if (userMissionChatRooms.isEmpty()) {
            log.warn("채팅방을 찾을 수 없습니다. chatRoomId : {}", chatRoomId);
            throw new EntityNotFoundException(ErrorCode.NOT_FOUND_MISSION_CHATROOM);
        }

        return userMissionChatRooms;
    }
}
