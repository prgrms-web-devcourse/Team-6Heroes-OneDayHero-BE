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

        log.info("redis에서 topic을 불러왔습니다. {}", topic.getTopic());
        log.info("message의 타입은 {} 입니다.", message.messageType().name());

        if (message.messageType().isLeave()) {
            message = ChatMessageMapper.toLeaveMessage(message);
            log.info("나가기 메시지로 변경되었습니다.");
        }

        publisher.publish(topic, message);

        var chatMessage = message.toEntity(serverTime);
        chatMessageRepository.save(chatMessage);
        log.info("채팅 메시지가 저장되었습니다.");
    }

    public List<ChatMessageApiResponse> findMessageByChatRoomId(
            Long chatRoomId
    ) {
        redisRepository.enterChatRoom(chatRoomId);
        var chatRoomsMessages = chatMessageRepository.findAllByChatRoomId(chatRoomId);

        return chatRoomsMessages.stream()
                .map(ChatMessageApiResponse::from)
                .toList();
    }
}
