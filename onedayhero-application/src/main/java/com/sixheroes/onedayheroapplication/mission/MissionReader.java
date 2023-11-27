package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.mission.repository.MissionQueryRepository;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionCompletedEventQueryResponse;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionQueryResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class MissionReader {

    private final MissionRepository missionRepository;
    private final MissionQueryRepository missionQueryRepository;

    public Mission findOne(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션 아이디가 입력되었습니다. id : {}", missionId);
                    return new NoSuchElementException(ErrorCode.EM_008.name());
                });
    }

    public MissionQueryResponse fetchFindOne(
            Long missionId,
            Long userId
    ) {
        return missionQueryRepository.fetchOne(missionId, userId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션 아이디가 입력되었습니다. id : {}", missionId);
                    return new NoSuchElementException(ErrorCode.EM_008.name());
                });
    }

    public MissionCompletedEventQueryResponse findMissionCompletedEvent(
        Long missionId
    ) {
        return missionQueryRepository.findMissionCompletedEvent(missionId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 미션 아이디가 입력되었습니다. id : {}", missionId);
                return new NoSuchElementException(ErrorCode.EM_008.name());
            });
    }
}
