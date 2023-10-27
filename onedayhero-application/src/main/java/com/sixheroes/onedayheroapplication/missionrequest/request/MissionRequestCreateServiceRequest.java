package com.sixheroes.onedayheroapplication.missionrequest.request;

import lombok.Builder;

@Builder
public record MissionRequestCreateServiceRequest(
    Long missionId,
    Long heroId
) {
}
