package com.sixheroes.onedayheroapi.mission.request;

import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionBookmarkCreateRequest(
        @NotNull(message = "미션 아이디는 필수 값 입니다.")
        Long missionId
) {

    public MissionBookmarkCreateServiceRequest toService() {
        return MissionBookmarkCreateServiceRequest.builder()
                .missionId(missionId)
                .build();
    }
}
