package com.sixheroes.onedayheroapplication.missionrequest;

import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestApproveServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestRejectServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestApproveResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestRejectResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MissionRequestService {

    @Transactional
    public MissionRequestCreateResponse createMissionRequest(
        MissionRequestCreateServiceRequest request
    ) {
        return null;
    }

    @Transactional
    public MissionRequestApproveResponse approveMissionRequest(
        Long missionRequestId,
        MissionRequestApproveServiceRequest missionRequestApproveServiceRequest
    ) {
        // TODO 유저가 요청받은 히어로 인지 확인, 미션 매칭 중인지 확인
        return null;
    }

    @Transactional
    public MissionRequestRejectResponse rejectMissionRequest(
        Long missionRequestId,
        MissionRequestRejectServiceRequest missionRequestRejectServiceRequest
    ) {
        return null;
    }
}
