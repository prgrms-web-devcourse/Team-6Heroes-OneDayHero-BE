package com.sixheroes.onedayheroapi.missionproposal.request;

import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalApproveServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionProposalApproveRequest(
        @NotNull(message = "유저 아이디는 필수 값 입니다.")
        Long userId
) {
    public MissionProposalApproveServiceRequest toService() {
        return new MissionProposalApproveServiceRequest(userId);
    }
}
