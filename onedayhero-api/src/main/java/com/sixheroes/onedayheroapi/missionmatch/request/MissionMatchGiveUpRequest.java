package com.sixheroes.onedayheroapi.missionmatch.request;

import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchGiveUpServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionMatchGiveUpRequest(
        @NotNull(message = "히어로 아이디는 필수 값 입니다.")
        Long heroId,

        @NotNull(message = "미션 아이디는 필수 값 입니다.")
        Long missionId
) {

    public MissionMatchGiveUpServiceRequest toService() {
        return MissionMatchGiveUpServiceRequest.builder()
                .heroId(heroId)
                .missionId(missionId)
                .build();
    }
}
