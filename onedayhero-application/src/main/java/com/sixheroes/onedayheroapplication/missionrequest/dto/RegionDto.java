package com.sixheroes.onedayheroapplication.missionrequest.dto;

import lombok.Builder;

@Builder
public record RegionDto(
        String si,
        String gu,
        String dong
) {
}
