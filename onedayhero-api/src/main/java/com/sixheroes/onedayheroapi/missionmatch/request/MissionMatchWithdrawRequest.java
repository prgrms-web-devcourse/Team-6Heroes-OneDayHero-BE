package com.sixheroes.onedayheroapi.missionmatch.request;

import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchWithdrawServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionMatchWithdrawRequest(
        @NotNull(message = "시민 아이디는 필수 값 입니다.")
        Long citizenId,

        @NotNull(message = "미션 아이디는 필수 값 입니다.")
        Long missionId
) {

    public MissionMatchWithdrawServiceRequest toService() {
        return MissionMatchWithdrawServiceRequest.builder()
                .citizenId(citizenId)
                .missionId(missionId)
                .build();
    }
}
