package com.sixheroes.onedayheroapi.mission.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.request.MissionExtendServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record MissionExtendRequest(
        @NotNull(message = "시민 아이디는 필수 값 입니다.")
        Long citizenId,

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
        LocalDateTime deadlineTime
) {

    public MissionExtendServiceRequest toService() {
        return MissionExtendServiceRequest.builder()
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .build();
    }
}
