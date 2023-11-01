package com.sixheroes.onedayheroapplication.region.response;

import com.sixheroes.onedayherodomain.region.Region;
import lombok.Builder;

@Builder
public record RegionResponse(
        Long id,
        String si,
        String gu,
        String dong
) {

    public static RegionResponse from(Region region) {
        return RegionResponse.builder()
                .id(region.getId())
                .si(region.getSi())
                .gu(region.getGu())
                .dong(region.getDong())
                .build();
    }
}
