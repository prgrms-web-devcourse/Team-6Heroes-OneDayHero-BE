package com.sixheroes.onedayherodomain.missionmatch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionMatchStatus {
    MATCHED("매칭완료"),
    CANCELED("취소"),
    COMPLETED("완료");

    private final String description;
}
