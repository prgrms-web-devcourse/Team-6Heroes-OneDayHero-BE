package com.sixheroes.onedayherochat.application.infra.redis.sub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisChatSubscriber implements MessageListener {

    private static final String CHAT_MESSAGE_SUB_URI = "/queue/chatRooms/";
    private static final String CHAT_TEMPLATE_NAME = "chatRedisTemplate";

    private final ObjectMapper objectMapper;

    @Resource(name = CHAT_TEMPLATE_NAME)
    private final RedisTemplate redisTemplate;

    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(
            Message message,
            byte[] pattern
    ) {
        try {
            var publishMessage = (String) redisTemplate.getStringSerializer()
                    .deserialize(message.getBody());

            var chatMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);

            // 구독자들에게 채팅 메세지 전송
            messagingTemplate.convertAndSend(CHAT_MESSAGE_SUB_URI + chatMessage.chatRoomId(), chatMessage);
        } catch (Exception e) {
            log.error(ErrorCode.S_001.name(), e);
        }
    }
}
