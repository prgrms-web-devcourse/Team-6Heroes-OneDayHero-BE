package com.sixheroes.onedayheroapi.chat.request;

import com.sixheroes.onedayheroapplication.chat.request.HandledChatMessage;

public record ChatMessage(
        Long senderId,
        String message,
        String chatType
) {

    public HandledChatMessage toHandler() {
        return HandledChatMessage.builder()
                .senderId(senderId)
                .message(message)
                .chatType(chatType)
                .build();
    }
}
