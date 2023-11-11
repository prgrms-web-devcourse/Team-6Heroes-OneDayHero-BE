package com.sixheroes.onedayheroapplication.chat.response;

public record ChatResponse(
        Long id,
        Long chatRoomId,
        String message,
        String chatType
) {
}
