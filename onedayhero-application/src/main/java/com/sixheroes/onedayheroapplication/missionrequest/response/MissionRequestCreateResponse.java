package com.sixheroes.onedayheroapplication.missionrequest.response;

import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import lombok.Builder;

@Builder
public record MissionRequestCreateResponse(
    Long missionRequestId,
    Long missionId,
    Long heroId,
    String missionRequestStatus
) {
    public static MissionRequestCreateResponse from(MissionRequest missionRequest) {
        return MissionRequestCreateResponse.builder()
            .missionRequestId(missionRequest.getId())
            .missionId(missionRequest.getMissionId())
            .heroId(missionRequest.getHeroId())
            .missionRequestStatus(missionRequest.getMissionRequestStatus().name())
            .build();
    }
}
