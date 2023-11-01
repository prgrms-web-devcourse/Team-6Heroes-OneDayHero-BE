package com.sixheroes.onedayherodomain.mission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionStatus {
    MATCHING("매칭 중"),
    MATCHING_COMPLETED("매칭 완료"),
    MISSION_COMPLETED("미션 완료"),
    EXPIRED("마감된 미션");

    private final String description;

    public boolean isMatching() {
        return this == MATCHING;
    }

    public boolean isMatchingCompleted() {
        return this == MATCHING_COMPLETED;
    }

    public boolean isExpired() {
        return this == EXPIRED;
    }
}
