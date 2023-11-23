package com.sixheroes.onedayherochat.application.infra.redis.handler;

import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisChatPublisher {

    private static final String CHAT_TEMPLATE_NAME = "chatRedisTemplate";

    @Resource(name = CHAT_TEMPLATE_NAME)
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(
            ChannelTopic topic,
            ChatMessageRequest message
    ) {
        log.info("레디스에 메시지를 발행합니다. {}", message);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
