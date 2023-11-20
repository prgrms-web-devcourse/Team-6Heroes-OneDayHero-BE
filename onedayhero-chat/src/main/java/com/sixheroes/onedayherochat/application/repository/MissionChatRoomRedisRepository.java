package com.sixheroes.onedayherochat.application.repository;

import com.sixheroes.onedayherochat.application.infra.redis.sub.RedisChatSubscriber;
import com.sixheroes.onedayherochat.application.repository.request.MissionChatRoomRedisRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MissionChatRoomRedisRepository {

    // 채팅방(topic)에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer messageListenerContainer;

    // 구독 처리 서비스
    private final RedisChatSubscriber redisSubscriber;

    // Redis
    private static final String CHAT_ROOMS = "chatRoom";
    private static final String CHAT_TEMPLATE_NAME = "chatRedisTemplate";

    @Resource(name = CHAT_TEMPLATE_NAME)
    private final RedisTemplate<String, Object> redisTemplate;

    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private HashOperations<String, Long, MissionChatRoomRedisRequest> opsHashChatRoom;

    private Map<Long, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public MissionChatRoomRedisRequest create(MissionChatRoomRedisRequest request) {
        opsHashChatRoom.put(CHAT_ROOMS, request.id(), request);
        return request;
    }

    public List<MissionChatRoomRedisRequest> findAll() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public Optional<MissionChatRoomRedisRequest> findById(Long missionChatRoomId) {
        return Optional.ofNullable(opsHashChatRoom.get(CHAT_ROOMS, missionChatRoomId));
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(Long chatRoomId) {
        ChannelTopic topic = topics.get(chatRoomId);
        if (topic == null) {
            topic = new ChannelTopic(String.valueOf(chatRoomId));
            messageListenerContainer.addMessageListener(redisSubscriber, topic);
            topics.put(chatRoomId, topic);
        }
    }

    public Optional<ChannelTopic> findTopicById(Long chatRoomId) {
        return Optional.ofNullable(topics.get(chatRoomId));
    }

    public void removeChatRoom(Long chatRoomId) {
        opsHashChatRoom.delete(CHAT_ROOMS, chatRoomId);
        topics.remove(chatRoomId);
    }
}
