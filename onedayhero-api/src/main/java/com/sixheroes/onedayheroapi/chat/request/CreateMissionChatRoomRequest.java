package com.sixheroes.onedayheroapi.chat.request;

import jakarta.validation.constraints.NotNull;

public record CreateMissionChatRoomRequest(
        @NotNull(message = "missionId는 필수 값 입니다.")
        Long missionId
) {
}
