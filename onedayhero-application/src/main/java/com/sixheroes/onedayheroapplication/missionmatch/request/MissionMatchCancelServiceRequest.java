package com.sixheroes.onedayheroapplication.missionmatch.request;

import lombok.Builder;

@Builder
public record MissionMatchCancelServiceRequest(
        Long missionId
) {
}
