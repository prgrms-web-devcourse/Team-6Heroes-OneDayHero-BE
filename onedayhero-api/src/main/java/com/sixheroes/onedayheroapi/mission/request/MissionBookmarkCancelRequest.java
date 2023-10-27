package com.sixheroes.onedayheroapi.mission.request;

import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;

public record MissionBookmarkCancelRequest(
        Long missionId,
        Long userId
) {

    public MissionBookmarkCancelServiceRequest toService() {
        return MissionBookmarkCancelServiceRequest.builder()
                .missionId(missionId)
                .userId(userId)
                .build();
    }
}
