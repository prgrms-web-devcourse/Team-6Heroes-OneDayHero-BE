package com.sixheroes.onedayheroapplication.region.response;

import com.sixheroes.onedayheroapplication.mission.repository.response.MissionQueryResponse;
import com.sixheroes.onedayherodomain.region.Region;
import lombok.Builder;

@Builder
public record RegionResponse(
        Long id,
        String si,
        String gu,
        String dong
) {

    public static RegionResponse from(
            MissionQueryResponse response
    ) {
        return RegionResponse.builder()
                .id(response.regionId())
                .si(response.si())
                .gu(response.gu())
                .dong(response.dong())
                .build();
    }

    public static RegionResponse from(
            Region region
    ) {
        return RegionResponse.builder()
                .id(region.getId())
                .si(region.getSi())
                .gu(region.getGu())
                .dong(region.getDong())
                .build();
    }
}
