package com.sixheroes.onedayheroapi.missionproposal.request;

import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalCreateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionProposalCreateRequest(
    @NotNull(message = "유저 아이디는 필수 값입니다.")
    Long userId,

    @NotNull(message = "미션 아이디는 필수 값입니다.")
    Long missionId,

    @NotNull(message = "히어로 아이디는 필수 값입니다.")
    Long heroId
) {

    public MissionProposalCreateServiceRequest toService() {
        return MissionProposalCreateServiceRequest.builder()
            .userId(userId)
            .missionId(missionId)
            .heroId(heroId)
            .build();
    }
}