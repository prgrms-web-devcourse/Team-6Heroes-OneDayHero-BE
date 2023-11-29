package com.sixheroes.onedayheroapi.mission.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.request.MissionInfoServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record MissionInfoRequest(
        @NotBlank(message = "미션의 제목은 필수 값이며 공백 일 수 없습니다.")
        String title,

        @NotBlank(message = "미션의 내용은 필수 값이며 공백 일 수 없습니다.")
        String content,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @NotNull(message = "미션 수행일은 필수 값 입니다.")
        LocalDate missionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        @NotNull(message = "미션 시작 시간은 필수 값 입니다.")
        LocalTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        @NotNull(message = "미션 종료 시간은 필수 값 입니다.")
        LocalTime endTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @NotNull(message = "미션 마감 시간은 필수 값 입니다.")
        LocalDateTime deadlineTime,

        @NotNull(message = "미션 포상금은 필수 값 입니다.")
        Integer price
) {

    public MissionInfoServiceRequest toService() {
        return MissionInfoServiceRequest.builder()
                .title(title)
                .content(content)
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(price)
                .build();
    }
}
