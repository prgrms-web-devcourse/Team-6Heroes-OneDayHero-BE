package com.sixheroes.onedayheroapplication.missionmatch;

import com.sixheroes.onedayheroapplication.mission.MissionReader;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchCreateEvent;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchRejectEvent;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCancelServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import com.sixheroes.onedayherodomain.missionmatch.repository.MissionMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MissionMatchService {

    private final MissionMatchRepository missionMatchRepository;
    private final MissionReader missionReader;
    private final MissionMatchReader missionMatchReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MissionMatchResponse createMissionMatch(
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

        var missionMatchCreateEvent = MissionMatchCreateEvent.from(missionMatch);
        applicationEventPublisher.publishEvent(missionMatchCreateEvent);

        return MissionMatchResponse
                .builder()
                .id(savedMissionMatch.getId())
                .build();
    }

    public MissionMatchResponse cancelMissionMatch(
            Long userId,
            MissionMatchCancelServiceRequest request
    ) {
        var mission = missionReader.findOne(request.missionId());
        var missionMatch = missionMatchReader.findByMissionIdAndMatched(mission.getId());

        validateMissionMatchCancelRequestUser(userId, mission.getCitizenId(), missionMatch.getHeroId());
        mission.cancelMissionMatching(mission.getCitizenId());

        missionMatch.canceled();
        var missionMatchRejectEvent = MissionMatchRejectEvent.from(missionMatch);
        applicationEventPublisher.publishEvent(missionMatchRejectEvent);

        return MissionMatchResponse.builder()
                .id(missionMatch.getId())
                .build();
    }

    private void validateMissionMatchCancelRequestUser(
            Long userId,
            Long citizenId,
            Long heroId
    ) {
       if (!(Objects.equals(userId, citizenId) || Objects.equals(userId, heroId))) {
           throw new BusinessException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }
}
