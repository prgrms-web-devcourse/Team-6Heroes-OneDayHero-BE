package com.sixheroes.onedayheroapplication.missionproposal.response;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;

public record MissionProposalIdResponse(
    Long id
) {

    public static MissionProposalIdResponse from(
        MissionProposal missionProposal
    ) {
        return new MissionProposalIdResponse(missionProposal.getId());
    }
}
