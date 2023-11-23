package com.sixheroes.onedayheroapplication.chatroom.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.missionchatroom.repository.response.UserChatRoomQueryResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionChatRoomFindResponse(
        Long id,

        Long receiverId,

        String title,

        String receiverNickname,

        String receiverImagePath,

        String lastSentMessage,

        Integer headCount,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime lastSentMessageTime

) {
    public static MissionChatRoomFindResponse from(UserChatRoomQueryResponse queryResponse) {
        return MissionChatRoomFindResponse.builder()
                .id(queryResponse.chatRoomId())
                .receiverId(queryResponse.receiverId())
                .title(queryResponse.title())
                .receiverNickname(queryResponse.nickName())
                .receiverImagePath(queryResponse.path())
                .headCount(queryResponse.headCount())
                .build();
    }
}
