package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
public enum MissionCategoryCode {
    MC_001(1L, "서빙"),
    MC_002(2L, "주방"),
    MC_003(3L, "배달, 운전"),
    MC_004(4L, "카페"),
    MC_005(5L, "청소"),
    MC_006(6L, "행사"),
    MC_007(7L, "포장, 물류"),
    MC_008(8L, "기타");

    private final Long categoryId;
    private final String description;

    public static MissionCategoryCode from(
        String missionCategoryCode
    ) {
        return Arrays.stream(MissionCategoryCode.values())
            .filter(mc -> Objects.equals(mc.name(), missionCategoryCode))
            .findFirst()
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST_VALUE));
    }
}
