package com.sixheroes.onedayheroapplication.missionrequest;

import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestApproveServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestRejectServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestApproveResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestRejectResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestResponse;
import com.sixheroes.onedayheroapplication.user.UserReader;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionrequest.repository.MissionRequestRepository;
import com.sixheroes.onedayheroquerydsl.missionrequest.MissionRequestQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionRequestService {

    private final MissionRepository missionRepository;
    private final MissionRequestRepository missionRequestRepository;
    private final MissionRequestQueryRepository missionRequestQueryRepository;

    private final MissionRequestReader missionRequestReader;
    private final UserReader userReader;

    @Transactional
    public MissionRequestCreateResponse createMissionRequest(
        MissionRequestCreateServiceRequest request
    ) {
        validMission(request.missionId(), request.userId());
        validHero(request.heroId());

        var missionRequest = request.toEntity();
        var savedMissionRequest = missionRequestRepository.save(missionRequest);

        return MissionRequestCreateResponse.from(savedMissionRequest);
    }

    @Transactional
    public MissionRequestApproveResponse approveMissionRequest(
        Long missionRequestId,
        MissionRequestApproveServiceRequest missionRequestApproveServiceRequest
    ) {
        var missionRequest = missionRequestReader.findOne(missionRequestId);

        var missionId = missionRequest.getMissionId();
        // TODO MissionReader 생기면 변경
        var mission = missionRepository.findById(missionId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 미션입니다. missionId : {}", missionId);
                return new NoSuchElementException(ErrorCode.EMC_000.name());
            });

        mission.validMissionRequestChangeStatus();

        missionRequest.changeMissionRequestStatusApprove(missionRequestApproveServiceRequest.userId());

        return MissionRequestApproveResponse.from(missionRequest);
    }

    @Transactional
    public MissionRequestRejectResponse rejectMissionRequest(
        Long missionRequestId,
        MissionRequestRejectServiceRequest missionRequestRejectServiceRequest
    ) {
        var missionRequest = missionRequestReader.findOne(missionRequestId);

        var missionId = missionRequest.getMissionId();
        var mission = missionRepository.findById(missionId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 미션입니다. missionId : {}", missionId);
                return new NoSuchElementException(ErrorCode.EMC_000.name());
            });

        mission.validMissionRequestChangeStatus();

        missionRequest.changeMissionRequestStatusReject(missionRequestRejectServiceRequest.userId());

        return MissionRequestRejectResponse.from(missionRequest);
    }

    public MissionRequestResponse findMissionRequest(
            Long heroId,
            Pageable pageable
    ) {
        var slice = missionRequestQueryRepository.findByHeroIdAndPageable(heroId, pageable);
        return MissionRequestResponse.from(slice);
    }

    private void validMission(
        Long missionId,
        Long userId
    ) {
        var mission = missionRepository.findById(missionId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 미션입니다. missionId : {}", missionId);
                return new NoSuchElementException(ErrorCode.EMC_000.name());
            });

        mission.validMissionRequestPossible(userId);
    }

    private void validHero(
        Long heroId
    ) {
        var hero = userReader.findOne(heroId);

        hero.validPossibleMissionRequested();
    }
}
