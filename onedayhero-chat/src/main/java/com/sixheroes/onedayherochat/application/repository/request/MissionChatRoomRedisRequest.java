package com.sixheroes.onedayherochat.application.repository.request;

import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record MissionChatRoomRedisRequest(
        Long id,
        Long missionId
) implements Serializable {

    public static MissionChatRoomRedisRequest from(MissionChatRoom missionChatRoom) {
        return MissionChatRoomRedisRequest.builder()
                .id(missionChatRoom.getId())
                .missionId(missionChatRoom.getMissionId())
                .build();
    }
}
