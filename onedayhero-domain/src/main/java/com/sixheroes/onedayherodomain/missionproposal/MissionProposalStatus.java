package com.sixheroes.onedayherodomain.missionproposal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionProposalStatus {
    PROPOSAL("제안"),
    APPROVE("승인"),
    REJECT("거절");

    private final String description;

    public boolean isProposal() {
        return this == PROPOSAL;
    }
}
