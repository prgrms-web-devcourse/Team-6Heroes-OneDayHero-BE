package com.sixheroes.onedayheroapplication.region.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AllRegionResponse(
        String si,
        List<GuResponse> gu
) {

    @Builder
    public record GuResponse(
            String gu,
            List<DongResponse> dong
    ) {

    }

    @Builder
    public record DongResponse(
            Long id,
            String dong
    ) {

    }
}

