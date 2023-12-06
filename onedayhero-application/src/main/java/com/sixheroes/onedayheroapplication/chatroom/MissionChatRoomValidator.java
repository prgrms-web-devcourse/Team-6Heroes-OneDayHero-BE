package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.missionchatroom.repository.UserMissionChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionChatRoomValidator {

    private final UserMissionChatRoomRepository userMissionChatRoomRepository;

    public void duplicateMissionChatRoom(
            List<Long> userIds,
            Long missionId
    ) {
        if (userMissionChatRoomRepository.findByUserIdInAndMissionChatRoom_MissionId(userIds, missionId).size() == 2) {
            log.warn("이미 존재하는 채팅방입니다. userId : {}, missionId : {}", userIds, missionId);
            throw new BusinessException(ErrorCode.DUPLICATE_MISSION_CHATROOM);
        }
    }
}
