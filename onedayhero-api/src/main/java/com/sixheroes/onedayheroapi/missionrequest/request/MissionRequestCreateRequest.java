package com.sixheroes.onedayheroapi.missionrequest.request;

import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionRequestCreateRequest(
    @NotNull(message = "유저 아이디는 필수 값입니다.")
    Long userId,

    @NotNull(message = "미션 아이디는 필수 값입니다.")
    Long missionId,

    @NotNull(message = "히어로 아이디는 필수 값입니다.")
    Long heroId
) {

    public MissionRequestCreateServiceRequest toService() {
        return MissionRequestCreateServiceRequest.builder()
            .userId(userId)
            .missionId(missionId)
            .heroId(heroId)
            .build();
    }
}