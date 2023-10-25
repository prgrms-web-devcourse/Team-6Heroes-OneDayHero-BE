package com.sixheroes.onedayherodomain.missionmatch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionMatchStatus {
    MATCHED("매칭 됨"),
    GIVE_UP("포기"),
    WITHDRAW("철회"),
    COMPLETE("완료");

    private final String description;
}
