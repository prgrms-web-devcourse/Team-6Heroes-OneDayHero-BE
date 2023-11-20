package com.sixheroes.onedayherochat.application.repository;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class MissionChatRoomRedisReader {

    private final MissionChatRoomRedisRepository missionChatRoomRedisRepository;

    public ChannelTopic findOne(Long chatRoomId) {
        return missionChatRoomRedisRepository.findTopicById(chatRoomId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.T_001.name()));
    }
}
