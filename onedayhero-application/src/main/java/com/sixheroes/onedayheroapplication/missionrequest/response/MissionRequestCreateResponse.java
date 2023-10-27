package com.sixheroes.onedayheroapplication.missionrequest.response;

public record MissionRequestCreateResponse(
    Long missionRequestId,
    Long missionId,
    Long heroId,
    String missionRequestStatus
) {


}
