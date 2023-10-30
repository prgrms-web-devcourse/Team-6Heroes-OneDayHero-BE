package com.sixheroes.onedayheroapplication.missionmatch.request;

import lombok.Builder;

@Builder
public record MissionMatchGiveUpServiceRequest(
        Long heroId,
        Long missionId
) {
}
