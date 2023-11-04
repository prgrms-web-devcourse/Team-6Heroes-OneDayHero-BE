package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayheroquerydsl.mission.response.MissionBookmarkMeQueryResponse;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;


public record MissionBookmarkMeViewResponse(
    Slice<MissionBookmarkMeLineDto> missionBookmarkMeLineDtos
) {

    public static MissionBookmarkMeViewResponse from(
            Slice<MissionBookmarkMeQueryResponse> queryResponses
    ) {
        var content = queryResponses.getContent()
                .stream()
                .map(MissionBookmarkMeLineDto::from)
                .toList();

        var lineDtos = new SliceImpl<MissionBookmarkMeLineDto>(
                content,
                queryResponses.getPageable(),
                queryResponses.hasNext()
        );

        return new MissionBookmarkMeViewResponse(lineDtos);
    }
}
