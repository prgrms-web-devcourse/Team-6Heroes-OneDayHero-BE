package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayheroapplication.mission.repository.response.MissionBookmarkMeQueryResponse;
import org.springframework.data.domain.Slice;


public record MissionBookmarkMeViewResponse(
        Long userId,
        Slice<MissionBookmarkMeResponse> missionBookmarkMeResponses
) {

    public static MissionBookmarkMeViewResponse of(
            Long userId,
            Slice<MissionBookmarkMeQueryResponse> responses
    ) {
        var missionBookmarkMeResponses = responses.map(MissionBookmarkMeResponse::from);
        return new MissionBookmarkMeViewResponse(userId, missionBookmarkMeResponses);
    }
}
