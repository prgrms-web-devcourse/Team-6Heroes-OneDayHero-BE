package com.sixheroes.onedayherodomain.mongo.chat;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document("chat-messages")
public class ChatMessage {

    @Id
    private String id;

    private Long chatRoomId;

    private Long senderId;

    private String senderNickName;

    private String message;

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
