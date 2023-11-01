package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.region.Region;
import lombok.Builder;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionResponse(
        Long id,
        MissionCategoryResponse missionCategory,
        Long citizenId,
        RegionResponse region,
        Point location,
        MissionInfoResponse missionInfo,
        Integer bookmarkCount,
        String missionStatus
) {

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
                .location(mission.getLocation())
                .missionInfo(MissionInfoResponse.from(mission.getMissionInfo()))
                .bookmarkCount(mission.getBookmarkCount())
                .missionStatus(mission.getMissionStatus().name())
                .build();
    }

    @Builder
    public record MissionInfoResponse(
            String content,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate missionDate,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime startTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime endTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime deadlineTime,

            Integer price
    ) {
        public static MissionInfoResponse from(MissionInfo missionInfo) {
            return MissionInfoResponse.builder()
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
