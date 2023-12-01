package com.sixheroes.onedayheroapplication.missionmatch;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatchStatus;
import com.sixheroes.onedayherodomain.missionmatch.repository.MissionMatchRepository;
import com.sixheroes.onedayherodomain.missionmatch.repository.dto.MissionMatchEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class MissionMatchReader {

    private final MissionMatchRepository missionMatchRepository;

    public MissionMatch findByMissionIdAndMatched(Long missionId) {
        return missionMatchRepository.findByMissionIdAndMissionMatchStatus(missionId, MissionMatchStatus.MATCHED)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 미션입니다. missionId : {}", missionId);
                return new BusinessException(ErrorCode.INVALID_MATCHING_STATUS);
            });
    }

    public MissionMatchEventDto findMissionMatchEventSendCitizen(Long missionMatchId) {
        return missionMatchRepository.findMissionMatchEvenSendCitizenById(missionMatchId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 미션 매칭입니다. missionMatchId : {}", missionMatchId);
                return new BusinessException(ErrorCode.INVALID_MATCHING_STATUS);
            });
    }

    public MissionMatchEventDto findMissionMatchEventSendHero(Long missionMatchId) {
        return missionMatchRepository.findMissionMatchEventSendHeroById(missionMatchId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 미션 매칭입니다. missionMatchId : {}", missionMatchId);
                return new BusinessException(ErrorCode.INVALID_MATCHING_STATUS);
            });
    }
}
