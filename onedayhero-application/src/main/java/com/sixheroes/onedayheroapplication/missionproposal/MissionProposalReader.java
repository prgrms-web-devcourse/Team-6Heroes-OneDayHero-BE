package com.sixheroes.onedayheroapplication.missionproposal;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
                return new NoSuchElementException(ErrorCode.EMP_000.name());
            });
    }
}