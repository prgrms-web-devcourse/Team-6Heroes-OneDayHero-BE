package com.sixheroes.onedayheroapplication.missionproposal.event.dto;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;

public record MissionProposalApproveEvent(
    Long missionProposalId
) {

    public static MissionProposalApproveEvent from(
        MissionProposal missionProposal
    ) {
        return new MissionProposalApproveEvent(missionProposal.getId());
    }
}
