package com.sixheroes.onedayherochat.application;

import com.sixheroes.onedayherochat.application.infra.redis.handler.RedisChatPublisher;
import com.sixheroes.onedayherochat.application.mapper.ChatMessageMapper;
import com.sixheroes.onedayherochat.application.repository.MissionChatRoomRedisReader;
import com.sixheroes.onedayherochat.application.repository.MissionChatRoomRedisRepository;
import com.sixheroes.onedayherochat.application.response.ChatMessageApiResponse;
import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;
import com.sixheroes.onedayheromongo.chat.application.ChatMongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatService {

    private final MissionChatRoomRedisReader missionChatRoomRedisReader;
    private final RedisChatPublisher publisher;
    private final ChatMongoService chatMongoService;
    private final MissionChatRoomRedisRepository redisRepository;

    @Transactional
    public void send(
            Long chatRoomId,
            ChatMessageRequest message,
            LocalDateTime serverTime
    ) {
        var topic = missionChatRoomRedisReader.findOne(chatRoomId);

        if (message.messageType().isLeave()) {
            message = ChatMessageMapper.toLeaveMessage(message);
        }

        publisher.publish(topic, message);
        chatMongoService.save(message.toMongo(), serverTime);
    }

    public List<ChatMessageApiResponse> findMessageByChatRoomId(
            Long chatRoomId
    ) {
        redisRepository.enterChatRoom(chatRoomId);

        return chatMongoService.findAllByChatRoomId(chatRoomId)
                .stream()
                .map(ChatMessageApiResponse::from)
                .toList();
    }
}
