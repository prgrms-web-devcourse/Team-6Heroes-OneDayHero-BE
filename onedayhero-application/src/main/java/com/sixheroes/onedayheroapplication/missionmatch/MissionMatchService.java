package com.sixheroes.onedayheroapplication.missionmatch;

import com.sixheroes.onedayheroapplication.mission.MissionReader;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCancelServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCreateResponse;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCancelResponse;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import com.sixheroes.onedayherodomain.missionmatch.repository.MissionMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MissionMatchService {

    private final MissionMatchRepository missionMatchRepository;
    private final MissionReader missionReader;
    private final MissionMatchReader missionMatchReader;

    public MissionMatchCreateResponse createMissionMatch(
            Long userId,
            MissionMatchCreateServiceRequest request
    ) {
        var mission = missionReader.findOne(request.missionId());
        var missionMatch = MissionMatch.createMissionMatch(
                mission.getId(),
                request.heroId()
        );

        mission.completeMissionMatching(userId);
        var savedMissionMatch = missionMatchRepository.save(missionMatch);

        //TODO: 시민, 히어로에게 미션매칭 성사 알람

        return MissionMatchCreateResponse.from(savedMissionMatch);
    }

    public MissionMatchCancelResponse cancelMissionMatch(
            Long userId,
            MissionMatchCancelServiceRequest request
    ) {
        var mission = missionReader.findOne(request.missionId());
        var missionMatch = missionMatchReader.findByMissionId(mission.getId());

        mission.cancelMissionMatching(userId);
        missionMatch.canceled();

        //TODO: 히어로에게 미션매칭 취소 알람

        return MissionMatchCancelResponse.builder()
                .missionMatchId(missionMatch.getId())
                .citizenId(userId)
                .missionId(request.missionId())
                .build();
    }
}
