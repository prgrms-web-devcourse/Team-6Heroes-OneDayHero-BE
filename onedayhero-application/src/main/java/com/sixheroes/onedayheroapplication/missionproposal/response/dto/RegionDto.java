package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import lombok.Builder;

@Builder
public record RegionDto(
        String si,
        String gu,
        String dong
) {
}
