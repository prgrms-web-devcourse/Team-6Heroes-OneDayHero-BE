package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionQueryResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.region.Region;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record MissionResponse(
        Long id,
        MissionCategoryResponse missionCategory,
        Long citizenId,
        RegionResponse region,
        Double longitude,
        Double latitude,
        MissionInfoResponse missionInfo,
        Integer bookmarkCount,
        String missionStatus
) {

    public static MissionResponse from(MissionQueryResponse response) {
        return MissionResponse.builder()
                .id(response.id())
                .missionCategory(
                        MissionCategoryResponse.from(response)
                )
                .citizenId(response.citizenId())
                .region(
                        RegionResponse.from(response)
                )
                .longitude(response.location().getX())
                .latitude(response.location().getY())
                .missionInfo(
                        MissionInfoResponse.from(response)
                )
                .bookmarkCount(response.bookmarkCount())
                .missionStatus(response.missionStatus().name())
                .build();
    }

    public static MissionResponse from(Mission mission, Region region) {
        return MissionResponse.builder()
                .id(mission.getId())
                .missionCategory(
                        MissionCategoryResponse.from(mission.getMissionCategory())
                )
                .citizenId(mission.getCitizenId())
                .region(
                        RegionResponse.from(region)
                )
                .longitude(mission.getLocation().getX())
                .latitude(mission.getLocation().getY())
                .missionInfo(MissionInfoResponse.from(mission.getMissionInfo()))
                .bookmarkCount(mission.getBookmarkCount())
                .missionStatus(mission.getMissionStatus().name())
                .build();
    }

    @Builder
    public record MissionInfoResponse(
            String title,

            String content,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate missionDate,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime startTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime endTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime deadlineTime,

            Integer price
    ) {

        public static MissionInfoResponse from(
                MissionQueryResponse response
        ) {
            return MissionInfoResponse.builder()
                    .title(response.title())
                    .content(response.content())
                    .missionDate(response.missionDate())
                    .startTime(response.startTime())
                    .endTime(response.endTime())
                    .deadlineTime(response.deadlineTime())
                    .price(response.price())
                    .build();
        }

        public static MissionInfoResponse from(
                MissionInfo missionInfo
        ) {
            return MissionInfoResponse.builder()
                    .title(missionInfo.getTitle())
                    .content(missionInfo.getContent())
                    .missionDate(missionInfo.getMissionDate())
                    .startTime(missionInfo.getStartTime())
                    .endTime(missionInfo.getEndTime())
                    .deadlineTime(missionInfo.getDeadlineTime())
                    .price(missionInfo.getPrice())
                    .build();
        }
    }
}
