package com.sixheroes.onedayheroapplication.missionproposal.request;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import lombok.Builder;

@Builder
public record MissionProposalCreateServiceRequest(
    Long userId,
    Long missionId,
    Long heroId
) {
    public MissionProposal toEntity() {
        return MissionProposal.builder()
            .missionId(missionId)
            .heroId(heroId)
            .build();
    }
}
