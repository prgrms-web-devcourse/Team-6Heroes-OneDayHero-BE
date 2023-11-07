package com.sixheroes.onedayheroapplication.missionproposal.response;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import lombok.Builder;

@Builder
public record MissionProposalApproveResponse(
    Long missionProposalId,
    Long missionId,
    Long heroId,
    String missionProposalStatus
) {
    public static MissionProposalApproveResponse from(
        MissionProposal missionProposal
    ) {
        return MissionProposalApproveResponse.builder()
            .missionProposalId(missionProposal.getId())
            .missionId(missionProposal.getMissionId())
            .heroId(missionProposal.getHeroId())
            .missionProposalStatus(missionProposal.getMissionProposalStatus().name())
            .build();
    }
}
