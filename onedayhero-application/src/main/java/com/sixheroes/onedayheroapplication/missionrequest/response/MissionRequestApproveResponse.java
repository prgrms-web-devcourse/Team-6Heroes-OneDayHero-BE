package com.sixheroes.onedayheroapplication.missionrequest.response;

import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import lombok.Builder;

@Builder
public record MissionRequestApproveResponse(
    Long missionRequestId,
    Long missionId,
    Long heroId,
    String missionRequestStatus
) {
    public static MissionRequestApproveResponse from(
        MissionRequest missionRequest
    ) {
        return MissionRequestApproveResponse.builder()
            .missionRequestId(missionRequest.getId())
            .missionId(missionRequest.getMissionId())
            .heroId(missionRequest.getHeroId())
            .missionRequestStatus(missionRequest.getMissionRequestStatus().name())
            .build();

    }
}
