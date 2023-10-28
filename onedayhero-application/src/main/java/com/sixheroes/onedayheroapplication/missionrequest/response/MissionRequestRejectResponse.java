package com.sixheroes.onedayheroapplication.missionrequest.response;

public record MissionRequestRejectResponse(
    Long missionRequestId,
    Long missionId,
    Long heroId,
    String missionRequestStatus
) {
}
