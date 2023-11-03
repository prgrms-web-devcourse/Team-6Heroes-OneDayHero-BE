package com.sixheroes.onedayheroapplication.missionrequest.response;

import com.sixheroes.onedayheroapplication.missionrequest.dto.MissionRequestDto;
import com.sixheroes.onedayheroinfraquerydsl.missionrequest.dto.MissionRequestQueryDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public record MissionRequestResponse (
        Slice<MissionRequestDto> missionRequests
) {
    public static MissionRequestResponse from(
            Slice<MissionRequestQueryDto> slice
    ) {
        var content = slice.getContent().stream()
                .map(MissionRequestDto::from)
                .toList();
        var missionRequests = new SliceImpl<MissionRequestDto>(content, slice.getPageable(), slice.hasNext());
        return new MissionRequestResponse(missionRequests);
    }
}
