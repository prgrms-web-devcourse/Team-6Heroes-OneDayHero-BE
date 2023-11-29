package com.sixheroes.onedayherochat.application.repository;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionChatRoomRedisReader {

    private final MissionChatRoomRedisRepository missionChatRoomRedisRepository;

    public ChannelTopic findOne(
            Long chatRoomId
    ) {
        return missionChatRoomRedisRepository.findTopicById(chatRoomId)
                .orElseThrow(() -> {
                    log.warn("채팅방 TOPIC을 찾지 못하였습니다.");
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_CHAT_TOPIC);
                });
    }
}
