package com.sixheroes.onedayherodomain.mission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionCategoryCode {
    MC_001("서빙"),
    MC_002("주방"),
    MC_003("배달, 운전"),
    MC_004("카페"),
    MC_005("청소"),
    MC_006("헬스"),
    MC_007("포장, 물류"),
    MC_008("기타");

    private final String description;
}
