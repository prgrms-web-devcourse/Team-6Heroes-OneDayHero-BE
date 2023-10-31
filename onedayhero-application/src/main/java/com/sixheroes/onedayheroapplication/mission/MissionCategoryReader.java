package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionCategoryReader {

    private final MissionCategoryRepository missionCategoryRepository;

    public MissionCategory findOne(Long missionCategoryId) {
        return missionCategoryRepository.findById(missionCategoryId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 미션 카테고리가 입력되었습니다. id : {}", missionCategoryId);
                    return new NoSuchElementException(ErrorCode.EMC_001.name());
                });
    }
}
