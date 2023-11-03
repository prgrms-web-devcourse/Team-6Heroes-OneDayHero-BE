package com.sixheroes.onedayheroapi.missionrequest.request;

import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestApproveServiceRequest;
import jakarta.validation.constraints.NotNull;

public record MissionRequestApproveRequest(
        @NotNull(message = "유저 아이디는 필수 값 입니다.")
        Long userId
) {
    public MissionRequestApproveServiceRequest toService() {
        return new MissionRequestApproveServiceRequest(userId);
    }
}
