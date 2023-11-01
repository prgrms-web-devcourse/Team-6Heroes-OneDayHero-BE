package com.sixheroes.onedayheroapplication.missionmatch;

import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCancelServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCreateResponse;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCancelResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import com.sixheroes.onedayherodomain.missionmatch.repository.MissionMatchRepository;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MissionMatchService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final MissionMatchRepository missionMatchRepository;
    private final MissionMatchReader missionMatchReader;

    public MissionMatchCreateResponse createMissionMatch(MissionMatchCreateServiceRequest request) {
        var mission = missionRepository.findById(request.missionId())
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션입니다. missionId : {}", request.missionId());
                    return new NoSuchElementException(ErrorCode.EMC_000.name());
                });

//        var hero = userRepository.findById(request.heroId())
//                .orElseThrow(() -> {
//                    log.debug("존재하지 않는 히어로입니다. userId : {}", request.heroId());
//                    return new NoSuchElementException(ErrorCode.EUC_000.name());
//                });

        var missionMatch = MissionMatch.createMissionMatch(
                mission.getId(),
                request.heroId()
        );

        mission.missionMatchingCompleted(request.userId());
        var savedMissionMatch = missionMatchRepository.save(missionMatch);

        //TODO: 시민, 히어로에게 미션매칭 성사 알람

        return MissionMatchCreateResponse.from(savedMissionMatch);
    }

    //시민이 미션 매칭 취소
    public MissionMatchCancelResponse cancelMissionMatch(MissionMatchCancelServiceRequest request) {
        var mission = missionRepository.findById(request.missionId())
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 미션입니다. missionId : {}", request.missionId());
                    return new NoSuchElementException(ErrorCode.EMC_000.name());
                });
        mission.missionMatchingCanceled(request.citizenId());

        var missionMatch = missionMatchReader.findByMissionId(mission.getId());
        missionMatch.canceled();

        //TODO: 히어로에게 미션매칭 취소 알람

        return MissionMatchCancelResponse.builder()
                .id(missionMatch.getId())
                .citizenId(request.citizenId())
                .missionId(request.missionId())
                .build();
    }
}
