package com.sixheroes.onedayheroapplication.chatroom.repository.response;

public record UserMissionChatRoomQueryResponse(
        Long id,
        Long chatRoomId,
        Long missionId,
        Integer headCount,
        String title
) {
}
