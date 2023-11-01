package com.sixheroes.onedayheroapi.mission.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MissionDeleteRequest(
        @NotNull(message = "시민 아이디는 필수 값 입니다.")
        Long citizenId
) {
}
