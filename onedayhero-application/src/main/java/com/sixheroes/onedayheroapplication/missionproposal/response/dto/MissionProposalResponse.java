package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.sixheroes.onedayheroquerydsl.missionproposal.dto.MissionProposalQueryDto;

public record MissionProposalResponse(
    Long id,
    MissionForMissionProposalResponse mission
) {

    public static MissionProposalResponse from(
        MissionProposalQueryDto missionProposalQueryDto
    ) {
        var mission = MissionForMissionProposalResponse.from(missionProposalQueryDto);

        return new MissionProposalResponse(missionProposalQueryDto.missionProposalId(), mission);
    }
}