package com.sixheroes.onedayherochat.presentation.request;

import com.sixheroes.onedayheromongo.application.chat.request.MongoChatMessage;

public record ChatMessageRequest(
        Long chatRoomId,
        Long senderId,
        String senderNickName,
        String message
) {

    public MongoChatMessage toMongo() {
        return MongoChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .senderNickName(senderNickName)
                .message(message)
                .build();
    }
}
