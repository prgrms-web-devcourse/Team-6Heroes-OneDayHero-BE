package com.sixheroes.onedayheroapi.missionrequest.request;

import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalRejectServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionProposalRejectRequest(
        @NotNull(message = "유저 아이디는 필수 값 입니다.")
        Long userId
) {
    public MissionProposalRejectServiceRequest toService() {
        return new MissionProposalRejectServiceRequest(userId);
    }
}
