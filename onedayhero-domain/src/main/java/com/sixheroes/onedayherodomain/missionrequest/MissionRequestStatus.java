package com.sixheroes.onedayherodomain.missionrequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionRequestStatus {
    REQUEST("제안"),
    APPROVE("승인"),
    REJECT("거절");

    private final String description;
}
