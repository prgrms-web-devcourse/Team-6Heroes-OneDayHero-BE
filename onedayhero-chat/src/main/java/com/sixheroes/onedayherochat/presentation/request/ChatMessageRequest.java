package com.sixheroes.onedayherochat.presentation.request;

public record ChatMessageRequest(
        Long chatRoomId,
        String senderId,
        String message
) {
}
