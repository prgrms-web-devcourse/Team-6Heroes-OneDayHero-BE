package com.sixheroes.onedayheroapi.missionmatch.request;

import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCancelServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionMatchCancelRequest(
        @NotNull(message = "시민 아이디는 필수 값 입니다.")
        Long citizenId,

        @NotNull(message = "미션 아이디는 필수 값 입니다.")
        Long missionId
) {

    public MissionMatchCancelServiceRequest toService() {
        return MissionMatchCancelServiceRequest.builder()
                .citizenId(citizenId)
                .missionId(missionId)
                .build();
    }
}
