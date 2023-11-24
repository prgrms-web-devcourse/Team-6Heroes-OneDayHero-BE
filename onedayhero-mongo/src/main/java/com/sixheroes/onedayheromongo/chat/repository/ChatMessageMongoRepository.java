package com.sixheroes.onedayheromongo.chat.repository;

import com.sixheroes.onedayheromongo.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ChatMessageMongoRepository {

    private final MongoTemplate mongoTemplate;

    public List<ChatMessage> findLatestMessagesByChatRoomIds(
            List<Long> chatRoomIds
    ) {
        List<ChatMessage> latestMessages = new ArrayList<>();

        for (Long chatRoomId : chatRoomIds) {
            Query query = new Query(Criteria.where("chat_room_id").is(chatRoomId));
            query.with(Sort.by(Sort.Direction.DESC, "sent_message_time"));
            query.limit(1);
            ChatMessage latestMessage = mongoTemplate.findOne(query, ChatMessage.class);
            if (latestMessage != null) {
                latestMessages.add(latestMessage);
            }
        }

        return latestMessages;
    }
}
