package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionReader {

    private final MissionRepository missionRepository;

    public Mission findOne(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 미션 아이디가 입력되었습니다. id : {}", missionId);
                    return new NoSuchElementException(ErrorCode.EM_008.name());
                });
    }
}