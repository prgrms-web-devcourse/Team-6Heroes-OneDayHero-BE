package com.sixheroes.onedayherodomain.missionapproval;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionApprovalStatus {
    REQUEST("요청"),
    APPROVE("승인"),
    REJECT("거절");

    private final String description;
}