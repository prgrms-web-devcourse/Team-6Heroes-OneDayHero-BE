package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayheroinfrachat.repository.CustomMissionChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionChatRoomReader {

    private final CustomMissionChatRoomRepository missionChatRoomRepository;

    public MissionChatRoom findById(
            Long missionChatRoomId
    ) {
        return missionChatRoomRepository.findById(missionChatRoomId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.T_001.name()));
    }
}
