package com.sixheroes.onedayherochat.application.infra.redis.pub;

import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisChatPublisher {

    private static final String CHAT_TEMPLATE_NAME = "chatRedisTemplate";

    // Autowired 와 유사하나 객체가 아닌 Bean 이름으로 검색
    // 향후 여러가지 Template가 생길 수 있으므로, 확장성 고려
    @Resource(name = CHAT_TEMPLATE_NAME)
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(
            ChannelTopic topic,
            ChatMessageRequest message
    ) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
