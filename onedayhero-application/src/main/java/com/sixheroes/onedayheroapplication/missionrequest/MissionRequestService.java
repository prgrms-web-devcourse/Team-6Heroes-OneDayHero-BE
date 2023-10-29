package com.sixheroes.onedayheroapplication.missionrequest;

import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestApproveResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestRejectResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionrequest.repository.MissionRequestRepository;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MissionRequestService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final MissionRequestRepository missionRequestRepository;

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
        var missionRequest = missionRequestRepository.findById(missionRequestId)
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.EMR_000.name()));

        var mission = missionRepository.findById(missionRequest.getMissionId())
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.EMC_000.name()));

        mission.validMissionRequestApprove();

        missionRequest.changeMissionRequestStatusApprove(missionRequestApproveServiceRequest.userId());

        return MissionRequestApproveResponse.from(missionRequest);
    }

    @Transactional
    public MissionRequestRejectResponse rejectMissionRequest(
        Long missionRequestId,
        MissionRequestRejectServiceRequest missionRequestRejectServiceRequest
    ) {
        return null;
    }

    private void validMission(
        Long missionId,
        Long userId
    ) {
        var mission = missionRepository.findById(missionId)
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.EMC_000.name()));

        mission.validMissionRequestPossible(userId);
    }

    private void validHero(
        Long heroId
    ) {
        var hero = userRepository.findById(heroId)
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.EUC_001.name()));

        hero.validPossibleMissionRequested();
    }
}
