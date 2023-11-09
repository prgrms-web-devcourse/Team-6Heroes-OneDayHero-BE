package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayheroquerydsl.mission.response.MissionBookmarkMeQueryResponse;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;


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
