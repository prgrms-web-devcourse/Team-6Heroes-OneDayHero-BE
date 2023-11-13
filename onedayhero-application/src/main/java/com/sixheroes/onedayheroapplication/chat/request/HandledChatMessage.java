package com.sixheroes.onedayheroapplication.chat.request;

import lombok.Builder;

@Builder
public record HandledChatMessage(
        Long id,
        String sender,
        String message,
        String type
) {

    public static HandledChatMessage createEnterHandledChatMessage(HandledChatMessage request) {
        return HandledChatMessage.builder()
                .id(request.id())
                .sender(request.sender)
                .message(request.sender + "님이 입장하셨습니다.")
                .type(request.type)
                .build();
    }
}
