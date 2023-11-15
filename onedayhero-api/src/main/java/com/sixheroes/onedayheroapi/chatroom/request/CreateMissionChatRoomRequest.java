package com.sixheroes.onedayheroapi.chatroom.request;

import com.sixheroes.onedayheroapplication.chatroom.request.CreateMissionChatRoomServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateMissionChatRoomRequest(
        @NotNull(message = "missionId는 필수 값 입니다.")
        Long missionId,

        @NotEmpty
        List<Long> userIds
) {

    public CreateMissionChatRoomServiceRequest toService() {
        return CreateMissionChatRoomServiceRequest.builder()
                .missionId(missionId)
                .userIds(userIds)
                .build();
    }
}
