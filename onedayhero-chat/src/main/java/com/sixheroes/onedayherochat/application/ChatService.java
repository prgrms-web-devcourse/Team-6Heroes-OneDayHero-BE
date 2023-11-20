package com.sixheroes.onedayherochat.application;

import com.sixheroes.onedayherochat.application.infra.redis.pub.RedisChatPublisher;
import com.sixheroes.onedayherochat.application.repository.MissionChatRoomRedisReader;
import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final MissionChatRoomRedisReader missionChatRoomRedisReader;
    private final RedisChatPublisher publisher;

    public void send(Long chatRoomId, ChatMessageRequest message) {
        var topic = missionChatRoomRedisReader.findOne(chatRoomId);
        publisher.publish(topic, message);
    }
}
