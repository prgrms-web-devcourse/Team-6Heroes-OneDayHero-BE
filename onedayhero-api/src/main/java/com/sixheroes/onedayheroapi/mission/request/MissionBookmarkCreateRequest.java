package com.sixheroes.onedayheroapi.mission.request;

import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;

public record MissionBookmarkCreateRequest(
        Long missionId,
        Long userId
) {

    public MissionBookmarkCreateServiceRequest toService() {
        return MissionBookmarkCreateServiceRequest.builder()
                .missionId(missionId)
                .userId(userId)
                .build();
    }
}
