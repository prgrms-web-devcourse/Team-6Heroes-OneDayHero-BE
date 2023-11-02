package com.sixheroes.onedayheroapplication.mission.request;

import org.springframework.util.MultiValueMap;

import java.util.List;

public record MissionFindFilterServiceRequest(
        List<String> missionCategoryCodes,
        List<String> missionDates,
        List<String> regionIds
) {

    public static MissionFindFilterServiceRequest from(MultiValueMap<String, String> paramMap) {
        var missionCategoryCodes = paramMap.get("missionCategoryCode");
        var missionDates = paramMap.get("missionDate");
        var regionIds = paramMap.get("region");

        return new MissionFindFilterServiceRequest(missionCategoryCodes, missionDates, regionIds);
    }
}
