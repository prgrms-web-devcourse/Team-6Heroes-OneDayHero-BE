package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.missionproposal.repository.dto.MissionProposalQueryDto;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionInfoForMissionProposalResponse(
        String title,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime endTime,

        Integer price
) {

    public static MissionInfoForMissionProposalResponse from(
            MissionProposalQueryDto missionProposalQueryDto
    ) {
        return MissionInfoForMissionProposalResponse.builder()
                .title(missionProposalQueryDto.missionTitle())
                .missionDate(missionProposalQueryDto.missionDate())
                .startTime(missionProposalQueryDto.startTime())
                .endTime(missionProposalQueryDto.endTime())
                .price(missionProposalQueryDto.price())
                .build();
    }
}
