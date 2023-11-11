package com.sixheroes.onedayheroapplication.chatroom.response;

import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import lombok.Builder;

@Builder
public record MissionChatRoomResponse(
        Long id,
        Long missionId
) {

    public static MissionChatRoomResponse from(
            MissionChatRoom missionChatRoom
    ) {
        return MissionChatRoomResponse.builder()
                .id(missionChatRoom.getId())
                .missionId(missionChatRoom.getMissionId())
                .build();
    }
}
