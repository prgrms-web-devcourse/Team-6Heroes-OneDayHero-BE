package com.sixheroes.onedayheroapplication.chatroom.response;

import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import lombok.Builder;

@Builder
public record MissionChatRoomExitResponse(
        Long id,
        Long userId,
        Long missionId
) {

    public static MissionChatRoomExitResponse from(
            MissionChatRoom missionChatRoom,
            Long userId
    ) {
        return MissionChatRoomExitResponse.builder()
                .id(missionChatRoom.getId())
                .userId(userId)
                .missionId(missionChatRoom.getMissionId())
                .build();
    }
}
