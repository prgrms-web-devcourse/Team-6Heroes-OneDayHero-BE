package com.sixheroes.onedayheroapi.chatroom.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatRoomExitRequest(
        @NotNull(message = "userId는 필수 값 입니다.")
        Long userId
) {
}
