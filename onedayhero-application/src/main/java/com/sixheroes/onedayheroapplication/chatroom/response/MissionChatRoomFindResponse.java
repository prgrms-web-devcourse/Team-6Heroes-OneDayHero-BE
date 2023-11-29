package com.sixheroes.onedayheroapplication.chatroom.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.missionchatroom.repository.response.UserChatRoomQueryResponse;
import com.sixheroes.onedayheromongo.chat.ChatMessage;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionChatRoomFindResponse(
        Long id,

        Long missionId,

        String missionStatus,

        Long receiverId,

        String title,

        String receiverNickname,

        String receiverImagePath,

        String lastSentMessage,

        Integer headCount,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime lastSentMessageTime

) {
    public static MissionChatRoomFindResponse from(
            UserChatRoomQueryResponse queryResponse,
            ChatMessage chatMessage
    ) {
        if (chatMessage == null) {
            return MissionChatRoomFindResponse.builder()
                    .id(queryResponse.chatRoomId())
                    .missionId(queryResponse.missionId())
                    .missionStatus(queryResponse.missionStatus().name())
                    .receiverId(queryResponse.receiverId())
                    .title(queryResponse.title())
                    .receiverNickname(queryResponse.nickName())
                    .receiverImagePath(queryResponse.path())
                    .lastSentMessage(null)
                    .headCount(queryResponse.headCount())
                    .lastSentMessageTime(null)
                    .build();
        }

        return MissionChatRoomFindResponse.builder()
                .id(queryResponse.chatRoomId())
                .missionId(queryResponse.missionId())
                .missionStatus(queryResponse.missionStatus().name())
                .receiverId(queryResponse.receiverId())
                .title(queryResponse.title())
                .receiverNickname(queryResponse.nickName())
                .receiverImagePath(queryResponse.path())
                .lastSentMessage(chatMessage.getMessage())
                .headCount(queryResponse.headCount())
                .lastSentMessageTime(chatMessage.getSentMessageTime())
                .build();
    }
}
