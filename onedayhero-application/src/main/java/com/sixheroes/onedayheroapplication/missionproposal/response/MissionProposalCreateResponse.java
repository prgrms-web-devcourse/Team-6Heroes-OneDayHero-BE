package com.sixheroes.onedayheroapplication.missionproposal.response;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import lombok.Builder;

@Builder
public record MissionProposalCreateResponse(
    Long id,
    Long missionId,
    Long heroId,
    String missionProposalStatus
) {
    public static MissionProposalCreateResponse from(
        MissionProposal missionProposal
    ) {
        return MissionProposalCreateResponse.builder()
            .id(missionProposal.getId())
            .missionId(missionProposal.getMissionId())
            .heroId(missionProposal.getHeroId())
            .missionProposalStatus(missionProposal.getMissionProposalStatus().name())
            .build();
    }
}
