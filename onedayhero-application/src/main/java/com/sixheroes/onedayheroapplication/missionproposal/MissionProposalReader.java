package com.sixheroes.onedayheroapplication.missionproposal;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import com.sixheroes.onedayherodomain.missionproposal.repository.dto.MissionProposalCreateEventDto;
import com.sixheroes.onedayherodomain.missionproposal.repository.dto.MissionProposalUpdateEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class MissionProposalReader {

    private final MissionProposalRepository missionProposalRepository;

    public MissionProposal findOne(
            Long missionProposalId
    ) {
        return missionProposalRepository.findById(missionProposalId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션 제안입니다. id : {}", missionProposalId);
                    throw new EntityNotFoundException(ErrorCode.INVALID_REQUEST_VALUE);
                });
    }

    public MissionProposalCreateEventDto findCreateEvent(
            Long missionProposalId
    ) {
        return missionProposalRepository.findMissionProposalCreateEventDtoById(missionProposalId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션 제안입니다. id : {}", missionProposalId);
                    throw new EntityNotFoundException(ErrorCode.INVALID_REQUEST_VALUE);
                });
    }

    public MissionProposalUpdateEventDto findUpdateEvent(
            Long missionProposalId
    ) {
        return missionProposalRepository.findMissionProposalUpdateEventDtoById(missionProposalId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션 제안입니다. id : {}", missionProposalId);
                    return new EntityNotFoundException(ErrorCode.INVALID_REQUEST_VALUE);
                });
    }
}