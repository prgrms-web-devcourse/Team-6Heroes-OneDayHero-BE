package com.sixheroes.onedayheroapplication.mission;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class MissionBookmarkReader {

    private final MissionBookmarkRepository missionBookmarkRepository;

    public MissionBookmark findById(Long missionBookmarkId) {
        return missionBookmarkRepository.findById(missionBookmarkId)
                .orElseThrow(() -> {
                    log.debug(
                            "존재하지 않는 미션 북마크입니다. missionBookmarkId:{}",
                            missionBookmarkId
                    );
                    return new NoSuchElementException(ErrorCode.EMC_000.name());
                });
    }

    public MissionBookmark findByMissionIdAndUserId(Long missionId, Long userId) {
        return missionBookmarkRepository.findByMissionIdAndUserId(missionId, userId)
                .orElseThrow(() -> {
                    log.debug(
                            "미션, 유저 아이디 값에 대해 존재하지 않는 미션 북마크입니다. missionId:{}, userId:{}",
                            missionId,
                            userId
                    );
                    return new NoSuchElementException(ErrorCode.EMC_000.name());
                });
    }
}

