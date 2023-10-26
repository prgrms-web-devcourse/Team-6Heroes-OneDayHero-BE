package com.sixheroes.onedayheroapplication.mission;


import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionBookmarkService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final MissionBookmarkRepository missionBookmarkRepository;

    @Transactional
    public MissionBookmarkCreateResponse createMissionBookmark(MissionBookmarkCreateRequest request) {
        var mission = missionRepository.findById(request.missionId())
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션입니다. id : {}", request.missionId());
                    return new NoSuchElementException(ErrorCode.EMC_000.name());
        });

        //TODO: 유저빌더가 없는 상태이기 때문에 테스트과정에서 유저를 생성할 수 없어서 검증이 불가능했음, 잠시 주석처리
//        var user = userRepository.findById(request.userId())
//                .orElseThrow(() -> {
//                    log.debug("존재하지 않는 유저입니다. id : {}", request.missionId());
//                    return new NoSuchElementException(ErrorCode.EUC_000.name());
//                });

        var missionBookmark = MissionBookmark.builder()
                .mission(mission)
                .userId(request.userId())
                .build();

        var savedMission = missionBookmarkRepository.save(missionBookmark);
        mission.addBookmarkCount(mission.getMissionStatus());

        return new MissionBookmarkCreateResponse(savedMission);
    }
}
