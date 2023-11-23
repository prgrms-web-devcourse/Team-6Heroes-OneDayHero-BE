package com.sixheroes.onedayheromongo.chat;

import com.sixheroes.onedayheromongo.global.BaseDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chat-messages")
public class ChatMessage extends BaseDocument {

    @MongoId
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
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
