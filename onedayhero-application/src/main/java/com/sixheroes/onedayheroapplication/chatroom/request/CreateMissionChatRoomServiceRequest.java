package com.sixheroes.onedayheroapplication.chatroom.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateMissionChatRoomServiceRequest(
        Long missionId,
        List<Long> userIds
) {
}
