package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.converter.PointConverter;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherodomain.mission.repository.response.MissionAroundQueryResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionAroundResponse(
        Long id,

        MissionCategoryResponse missionCategory,

        RegionResponse region,

        String title,

        Double longitude,

        Double latitude,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime endTime,

        Integer price,

        String imagePath
) {

    public static MissionAroundResponse from(
            MissionAroundQueryResponse response,
            String imagePath
    ) {
        return MissionAroundResponse.builder()
                .id(response.getId())
                .missionCategory(MissionCategoryResponse.builder()
                        .id(response.getMissionCategoryId())
                        .code(response.getMissionCategoryCode())
                        .name(response.getMissionCategoryName())
                        .build())
                .region(RegionResponse.builder()
                        .id(response.getRegionId())
                        .si(response.getSi())
                        .gu(response.getGu())
                        .dong(response.getDong())
                        .build())
                .longitude(Double.valueOf(PointConverter.convertPoint(response.getLocation())[1]))
                .latitude(Double.valueOf(PointConverter.convertPoint(response.getLocation())[0]))
                .missionDate(response.getMissionDate())
                .startTime(response.getStartTime())
                .endTime(response.getEndTime())
                .price(response.getPrice())
                .imagePath(imagePath)
                .build();
    }
}
