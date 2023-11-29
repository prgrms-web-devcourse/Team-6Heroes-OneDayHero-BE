package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.sixheroes.onedayheroapplication.missionproposal.repository.dto.MissionProposalQueryDto;
import lombok.Builder;

@Builder
public record RegionForMissionProposalResponse(
        String si,
        String gu,
        String dong
) {

    public static RegionForMissionProposalResponse from(
            MissionProposalQueryDto missionProposalQueryDto
    ) {
        return RegionForMissionProposalResponse.builder()
                .si(missionProposalQueryDto.si())
                .gu(missionProposalQueryDto.gu())
                .dong(missionProposalQueryDto.dong())
                .build();
    }
}
