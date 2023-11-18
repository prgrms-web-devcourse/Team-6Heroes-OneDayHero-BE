package com.sixheroes.onedayheroapi.mission.request;

import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionBookmarkCancelRequest(
        @NotNull(message = "미션 아이디는 필수 값 입니다.")
        Long missionId
) {

    public MissionBookmarkCancelServiceRequest toService() {
        return MissionBookmarkCancelServiceRequest.builder()
                .missionId(missionId)
                .build();
    }
}
