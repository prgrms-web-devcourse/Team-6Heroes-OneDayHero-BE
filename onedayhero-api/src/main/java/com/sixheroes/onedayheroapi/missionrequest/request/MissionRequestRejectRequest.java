package com.sixheroes.onedayheroapi.missionrequest.request;

import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestRejectServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionRequestRejectRequest(
        @NotNull(message = "유저 아이디는 필수 값 입니다.")
        Long userId
) {
    public MissionRequestRejectServiceRequest toService() {
        return new MissionRequestRejectServiceRequest(userId);
    }
}
