package com.sixheroes.onedayheroapplication.missionmatch;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import com.sixheroes.onedayherodomain.missionmatch.repository.MissionMatchRepository;
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

    public MissionMatch findByMissionId(Long missionId) {
        return missionMatchRepository.findByMissionId(missionId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션입니다. missionId : {}", missionId);
                    return new EntityNotFoundException(ErrorCode.INVALID_REQUEST_VALUE);
                });
    }
}
