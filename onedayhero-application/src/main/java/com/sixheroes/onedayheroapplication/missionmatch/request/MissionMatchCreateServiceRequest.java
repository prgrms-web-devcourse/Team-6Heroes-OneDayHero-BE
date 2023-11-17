package com.sixheroes.onedayheroapplication.missionmatch.request;

import lombok.Builder;

@Builder
public record MissionMatchCreateServiceRequest(
        Long missionId,
        Long heroId
) {
}
