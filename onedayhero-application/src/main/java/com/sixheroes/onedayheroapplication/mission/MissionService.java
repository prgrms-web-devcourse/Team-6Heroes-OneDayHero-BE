package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionService {

    private final MissionCategoryRepository missionCategoryRepository;
    private final MissionRepository missionRepository;

    public MissionResponse createMission(MissionCreateServiceRequest request) {
        var missionCategory = missionCategoryRepository.findById(request.missionCategoryId())
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 미션 카테고리가 입력되었습니다. id : {}", request.missionCategoryId());
                    return new NoSuchElementException(ErrorCode.EMC_001.name());
                });

        var mission = request.toEntity(missionCategory);
        mission.validRangeOfMissionTime(LocalDateTime.now());

        var savedMission = missionRepository.save(mission);

        return new MissionResponse(savedMission);
    }
}
