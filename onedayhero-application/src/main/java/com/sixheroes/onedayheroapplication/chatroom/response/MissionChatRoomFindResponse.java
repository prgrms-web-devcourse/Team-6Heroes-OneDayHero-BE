package com.sixheroes.onedayheroapplication.chatroom.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionChatRoomFindResponse(
        Long id,

        String title,

        String receiverNickname,

        String receiverImagePath,

        String lastSentMessage,

        Integer headCount,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime lastSentMessageTime

) {
}
