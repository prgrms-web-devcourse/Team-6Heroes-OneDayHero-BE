package com.sixheroes.onedayheroapplication.missionrequest.response;

import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import lombok.Builder;

@Builder
public record MissionRequestRejectResponse(
    Long missionRequestId,
    Long missionId,
    Long heroId,
    String missionRequestStatus
) {
    public static MissionRequestRejectResponse from(
        MissionRequest missionRequest
    ) {
        return MissionRequestRejectResponse.builder()
            .missionRequestId(missionRequest.getId())
            .missionId(missionRequest.getMissionId())
            .heroId(missionRequest.getHeroId())
            .missionRequestStatus(missionRequest.getMissionRequestStatus().name())
            .build();
    }
}
