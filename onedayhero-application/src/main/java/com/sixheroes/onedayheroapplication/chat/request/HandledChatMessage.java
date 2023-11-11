package com.sixheroes.onedayheroapplication.chat.request;

import lombok.Builder;

@Builder
public record HandledChatMessage(
        Long senderId,
        String message,
        String chatType
) {

    public static HandledChatMessage createEnterHandledChatMessage(HandledChatMessage request) {
        return HandledChatMessage.builder()
                .senderId(request.senderId)
                .message(request.senderId + "님이 입장하셨습니다.")
                .chatType(request.chatType)
                .build();
    }
}
