package com.sixheroes.onedayheroapplication.mission.request;

import lombok.Builder;

@Builder
public record MissionBookmarkCreateServiceRequest(
        Long missionId
) {
}
