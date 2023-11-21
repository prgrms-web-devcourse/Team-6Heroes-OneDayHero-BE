package com.sixheroes.onedayheromongo.chat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("chat-messages")
public class ChatMessage {

    @MongoId
    private String id;

    @Field(name = "chat_room_id")
    private Long chatRoomId;

    @Field(name = "sender_id")
    private Long senderId;

    @Field(name = "sender_nickname")
    private String senderNickName;

    @Field(name = "message")
    private String message;

    @Field(name = "sent_message_time")
    private LocalDateTime sentMessageTime;

    @Builder
    private ChatMessage(String id,
                        Long chatRoomId,
                        Long senderId,
                        String senderNickName,
                        String message,
                        LocalDateTime sentMessageTime
    ) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.senderNickName = senderNickName;
        this.message = message;
        this.sentMessageTime = sentMessageTime;
    }
}
