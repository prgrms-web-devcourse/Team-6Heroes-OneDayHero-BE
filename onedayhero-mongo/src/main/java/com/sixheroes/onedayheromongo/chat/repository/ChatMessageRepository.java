package com.sixheroes.onedayheromongo.chat.repository;

import com.sixheroes.onedayheromongo.chat.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findAllByChatRoomId(Long chatRoomId);
}
