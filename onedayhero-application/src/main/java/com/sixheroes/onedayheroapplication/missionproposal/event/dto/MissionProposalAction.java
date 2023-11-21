package com.sixheroes.onedayheroapplication.missionproposal.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionProposalAction {
    MISSION_PROPOSAL_CREATE("미션 제안 생성"),
    MISSION_PROPOSAL_APPROVE("미션 제안 승낙"),
    MISSION_PROPOSAL_REJECT("미션 제안 거절");

    private final String description;
}
