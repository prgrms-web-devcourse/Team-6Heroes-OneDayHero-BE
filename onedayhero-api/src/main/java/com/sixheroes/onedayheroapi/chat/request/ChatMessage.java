package com.sixheroes.onedayheroapi.chat.request;

import com.sixheroes.onedayheroapplication.chat.request.HandledChatMessage;

public record ChatMessage(
        Long id,
        String sender,
        String message,
        String type
) {

    public HandledChatMessage toHandler() {
        return HandledChatMessage.builder()
                .id(id)
                .sender(sender)
                .message(message)
                .type(type)
                .build();
    }
}
