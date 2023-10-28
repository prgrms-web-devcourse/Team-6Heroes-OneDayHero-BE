package com.sixheroes.onedayheroapplication.missionrequest.response;

public record MissionRequestApproveResponse(
    Long missionRequestId,
    Long missionId,
    Long heroId,
    String missionRequestStatus
) {
}
