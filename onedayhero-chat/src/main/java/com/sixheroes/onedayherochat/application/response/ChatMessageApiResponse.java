package com.sixheroes.onedayherochat.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheromongo.chat.ChatMessage;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageApiResponse(
        String senderNickName,

        String message,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime sentMessageTime
) {

    public static ChatMessageApiResponse from(
            ChatMessage chatMessage
    ) {
        return ChatMessageApiResponse.builder()
                .senderNickName(chatMessage.getSenderNickName())
                .message(chatMessage.getMessage())
                .sentMessageTime(chatMessage.getSentMessageTime())
                .build();
    }
}
