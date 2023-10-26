package com.sixheroes.onedayheroapi.mission.request;

import com.sixheroes.onedayheroapplication.mission.request.MissionInfoServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionInfoRequest(
        @NotBlank(message = "미션의 내용은 필수 값이며 공백 일 수 없습니다.")
        String content,

        @NotNull(message = "미션 수행일은 필수 값 입니다.")
        LocalDate missionDate,

        @NotNull(message = "미션 시작 시간은 필수 값 입니다.")
        LocalTime startTime,

        @NotNull(message = "미션 종료 시간은 필수 값 입니다.")
        LocalTime endTime,

        @NotNull(message = "미션 마감 시간은 필수 값 입니다.")
        LocalTime deadlineTime,

        @NotNull(message = "미션 포상금은 필수 값 입니다.")
        Integer price
) {

    public MissionInfoServiceRequest toService() {
        return MissionInfoServiceRequest.builder()
                .content(content)
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(price)
                .build();
    }
}
