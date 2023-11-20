package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayherocommon.error.ErrorCode;
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
            log.debug("이미 삭제 된 채팅방 아이디가 들어왔습니다.");
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }

        return userMissionChatRooms;
    }
}
