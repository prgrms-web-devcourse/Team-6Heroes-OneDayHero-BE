package com.sixheroes.onedayheroapplication.missionrequest.request;

import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import lombok.Builder;

@Builder
public record MissionRequestCreateServiceRequest(
    Long userId,
    Long missionId,
    Long heroId
) {
    public MissionRequest toEntity() {
        return MissionRequest.builder()
            .missionId(missionId)
            .heroId(heroId)
            .build();
    }
}
