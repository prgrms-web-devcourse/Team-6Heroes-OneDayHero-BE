package com.sixheroes.onedayheroapplication.missionrequest;

import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MissionRequestService {

    @Transactional
    public MissionRequestCreateResponse createMissionRequest(MissionRequestCreateServiceRequest request) {
        return null;
    }
}
