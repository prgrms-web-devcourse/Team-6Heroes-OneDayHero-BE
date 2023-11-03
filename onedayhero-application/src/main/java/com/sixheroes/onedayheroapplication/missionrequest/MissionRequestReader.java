package com.sixheroes.onedayheroapplication.missionrequest;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import com.sixheroes.onedayherodomain.missionrequest.repository.MissionRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class MissionRequestReader {

    private MissionRequestRepository missionRequestRepository;

    public MissionRequest findOne(
            Long missionRequestId
    ) {
        return missionRequestRepository.findById(missionRequestId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션 제안입니다. missionRequestId : {}", missionRequestId);
                    return new NoSuchElementException(ErrorCode.EMR_000.name());
                });
    }
}