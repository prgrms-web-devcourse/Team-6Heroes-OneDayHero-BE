package com.sixheroes.onedayheroapplication.missionproposal.response;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import lombok.Builder;

@Builder
public record MissionProposalCreateResponse(
    Long missionProposalId,
    Long missionId,
    Long heroId,
    String missionProposalStatus
) {
    public static MissionProposalCreateResponse from(
        MissionProposal missionProposal
    ) {
        return MissionProposalCreateResponse.builder()
            .missionProposalId(missionProposal.getId())
            .missionId(missionProposal.getMissionId())
            .heroId(missionProposal.getHeroId())
            .missionProposalStatus(missionProposal.getMissionProposalStatus().name())
            .build();
    }
}
