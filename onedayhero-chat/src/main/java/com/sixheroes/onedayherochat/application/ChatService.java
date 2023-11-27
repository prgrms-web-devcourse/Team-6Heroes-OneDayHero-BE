package com.sixheroes.onedayherochat.application;

import com.sixheroes.onedayherochat.application.infra.redis.handler.RedisChatPublisher;
import com.sixheroes.onedayherochat.application.mapper.ChatMessageMapper;
import com.sixheroes.onedayherochat.application.repository.MissionChatRoomRedisReader;
import com.sixheroes.onedayherochat.application.repository.MissionChatRoomRedisRepository;
import com.sixheroes.onedayherochat.application.response.ChatMessageApiResponse;
import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;
import com.sixheroes.onedayheromongo.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final MissionChatRoomRedisReader missionChatRoomRedisReader;
    private final RedisChatPublisher publisher;
    private final ChatMessageRepository chatMessageRepository;
    private final MissionChatRoomRedisRepository redisRepository;

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

        var chatMessage = message.toEntity(serverTime);
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageApiResponse> findMessageByChatRoomId(
            Long chatRoomId
    ) {
        var chatRoomsMessages = chatMessageRepository.findAllByChatRoomId(chatRoomId);
        redisRepository.enterChatRoom(chatRoomId);

        return chatRoomsMessages.stream()
                .map(ChatMessageApiResponse::from)
                .toList();
    }
}
