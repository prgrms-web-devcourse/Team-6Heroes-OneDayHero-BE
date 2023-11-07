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
        var content = responses.getContent()
                .stream()
                .map(MissionBookmarkMeResponse::from)
                .toList();

        var slice = new SliceImpl<MissionBookmarkMeResponse>(
                content,
                responses.getPageable(),
                responses.hasNext()
        );

        return new MissionBookmarkMeViewResponse(userId, slice);
    }
}
