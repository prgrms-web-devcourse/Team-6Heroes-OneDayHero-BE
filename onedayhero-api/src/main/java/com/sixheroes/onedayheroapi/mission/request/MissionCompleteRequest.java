package com.sixheroes.onedayheroapi.mission.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MissionCompleteRequest(
        @NotNull(message = "userId는 필수 값 입니다.")
        Long userId
) {
}
