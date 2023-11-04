package com.sixheroes.onedayheroapplication.mission.response;

import org.springframework.data.domain.Pageable;

import java.util.List;

public record MissionBookmarkMeViewResponse(
        Pageable pageable,
        List<MissionBookmarkMeLineDto> missionBookmarkMeLineDtos,
        boolean hasNext
) {
}
