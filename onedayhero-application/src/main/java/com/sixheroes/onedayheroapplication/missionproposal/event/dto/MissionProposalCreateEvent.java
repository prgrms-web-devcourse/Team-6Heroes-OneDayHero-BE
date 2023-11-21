package com.sixheroes.onedayheroapplication.missionproposal.event.dto;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import lombok.Builder;

@Builder
public record MissionProposalCreateEvent(
    Long missionProposalId
) {

    public static MissionProposalCreateEvent from(
        MissionProposal missionProposal
    ) {
        return new MissionProposalCreateEvent(missionProposal.getId());
    }
}
