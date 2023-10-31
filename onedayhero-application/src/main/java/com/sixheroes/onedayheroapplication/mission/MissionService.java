package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import com.sixheroes.onedayherodomain.bookmark.UserBookMarkMission;
import com.sixheroes.onedayherodomain.bookmark.repository.UserBookMarkMissionRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionService {

    private final MissionCategoryReader missionCategoryReader;
    private final MissionReader missionReader;
    private final MissionRepository missionRepository;
    private final UserBookMarkMissionRepository userBookMarkMissionRepository;

    @Transactional
    public MissionResponse createMission(
            MissionCreateServiceRequest request,
            LocalDateTime dateTime
    ) {
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var mission = request.toEntity(missionCategory);
        mission.validRangeOfMissionTime(dateTime);

        var savedMission = missionRepository.save(mission);

        return MissionResponse.from(savedMission);
    }

    @Transactional
    public void deleteMission(
            Long missionId,
            Long citizenId
    ) {
        var mission = missionReader.findOne(missionId);
        mission.validAbleDelete(citizenId);

        deleteUserBookMarkByMissionId(missionId);
        missionRepository.delete(mission);
    }

    @Transactional
    public MissionResponse updateMission(
            Long missionId,
            MissionUpdateServiceRequest request,
            LocalDateTime modifiedDateTime
    ) {
        var mission = missionReader.findOne(missionId);
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());

        var requestMission = request.toEntity(missionCategory);
        requestMission.validRangeOfMissionTime(modifiedDateTime);

        mission.update(requestMission);
        return MissionResponse.from(mission);
    }

    @Transactional
    public MissionResponse extendMission(
            MissionUpdateServiceRequest request,
            LocalDateTime dateTime
    ) {
        return null;
    }

    private void deleteUserBookMarkByMissionId(
            Long missionId
    ) {
        var userBookMarks = userBookMarkMissionRepository.findByMissionId(missionId);
        var userBookMarkIds = userBookMarks.stream()
                .map(UserBookMarkMission::getId)
                .toList();

        userBookMarkMissionRepository.deleteByIdIn(userBookMarkIds);
    }
}
