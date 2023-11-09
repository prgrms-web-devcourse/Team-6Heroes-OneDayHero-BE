package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.sixheroes.onedayheroquerydsl.missionproposal.dto.MissionProposalQueryDto;
import lombok.Builder;

@Builder
public record MissionCategoryForMissionProposalResponse(
    String code,
    String name
) {

    public static MissionCategoryForMissionProposalResponse from(
        MissionProposalQueryDto missionProposalQueryDto
    ) {
        return MissionCategoryForMissionProposalResponse.builder()
            .code(missionProposalQueryDto.missionCategoryCode().name())
            .name(missionProposalQueryDto.categoryName())
            .build();
    }
}
