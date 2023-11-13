package com.sixheroes.onedayheroapi.chat.request;

import jakarta.validation.constraints.NotNull;

public record CreateMissionChatRoomRequest(
        String roomName,

        @NotNull(message = "missionId는 필수 값 입니다.")
        Long missionId
) {
}
