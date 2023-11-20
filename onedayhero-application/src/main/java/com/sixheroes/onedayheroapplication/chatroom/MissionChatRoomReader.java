package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.repository.MissionChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionChatRoomReader {

    private final MissionChatRoomRepository missionChatRoomRepository;

    public MissionChatRoom findById(
            Long missionChatRoomId
    ) {
        return missionChatRoomRepository.findById(missionChatRoomId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 채팅방을 조회하였습니다. : {} ", missionChatRoomId);
                    return new NoSuchElementException(ErrorCode.T_001.name());
                });
    }
}
