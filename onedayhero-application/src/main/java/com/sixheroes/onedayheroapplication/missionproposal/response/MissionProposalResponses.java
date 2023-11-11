package com.sixheroes.onedayheroapplication.missionproposal.response;

import com.sixheroes.onedayheroapplication.missionproposal.response.dto.MissionProposalResponse;
import com.sixheroes.onedayheroquerydsl.missionproposal.dto.MissionProposalQueryDto;
import org.springframework.data.domain.Slice;

public record MissionProposalResponses(
    Slice<MissionProposalResponse> missionProposals
) {
    public static MissionProposalResponses from(
            Slice<MissionProposalQueryDto> slice
    ) {
        var sliceConverted = slice.map(MissionProposalResponse::from);

        return new MissionProposalResponses(sliceConverted);
    }
}
