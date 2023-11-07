package com.sixheroes.onedayheroapplication.missionproposal.response;

import com.sixheroes.onedayheroapplication.missionproposal.response.dto.MissionProposalDto;
import com.sixheroes.onedayheroquerydsl.missionproposal.dto.MissionProposalQueryDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public record MissionProposalResponse(
    Slice<MissionProposalDto> missionProposals
) {
    public static MissionProposalResponse from(
            Slice<MissionProposalQueryDto> slice
    ) {
        var content = slice.getContent().stream()
            .map(MissionProposalDto::from)
            .toList();
        var missionProposals = new SliceImpl<>(content, slice.getPageable(), slice.hasNext());
        return new MissionProposalResponse(missionProposals);
    }
}
