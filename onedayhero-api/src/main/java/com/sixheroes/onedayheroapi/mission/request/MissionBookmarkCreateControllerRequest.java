package com.sixheroes.onedayheroapi.mission.request;

import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateApplicationRequest;

public record MissionBookmarkCreateControllerRequest(
        Long missionId,
        Long userId
) {

    public MissionBookmarkCreateApplicationRequest mapToApplicationRequest() {
        return MissionBookmarkCreateApplicationRequest.builder()
                .missionId(missionId)
                .userId(userId)
                .build();
    }
}
