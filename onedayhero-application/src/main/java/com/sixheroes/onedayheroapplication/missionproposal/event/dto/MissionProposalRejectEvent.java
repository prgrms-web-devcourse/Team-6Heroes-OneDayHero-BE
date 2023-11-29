package com.sixheroes.onedayheroapplication.missionproposal.event.dto;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;

public record MissionProposalRejectEvent(
    Long missionProposalId
) {

    public static MissionProposalRejectEvent from(
        MissionProposal missionProposal
    ) {
        return new MissionProposalRejectEvent(missionProposal.getId());
    }
}
