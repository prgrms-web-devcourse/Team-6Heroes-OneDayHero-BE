package com.sixheroes.onedayheroapi.missionmatch.request;

import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionMatchCreateRequest(
        @NotNull(message = "유저 아이디는 필수 값 입니다.")
        Long userId,

        @NotNull(message = "미션 아이디는 필수 값 입니다.")
        Long missionId,

        @NotNull(message = "히어로 아이디는 필수 값 입니다.")
        Long heroId
) {

    public MissionMatchCreateServiceRequest toService() {
        return MissionMatchCreateServiceRequest.builder()
                .userId(userId)
                .missionId(missionId)
                .heroId(heroId)
                .build();
    }
}
