package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import com.sixheroes.onedayheroapplication.region.RegionReader;
import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayheroquerydsl.mission.MissionQueryRepository;
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
    private final RegionReader regionReader;
    private final MissionRepository missionRepository;
    private final MissionBookmarkRepository missionBookmarkRepository;
    private final MissionQueryRepository missionQueryRepository;

    @Transactional
    public MissionResponse createMission(
            MissionCreateServiceRequest request,
            LocalDateTime dateTime
    ) {
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var region = regionReader.findOne(request.regionId());
        var mission = request.toEntity(missionCategory);
        mission.validRangeOfMissionTime(dateTime);

        var savedMission = missionRepository.save(mission);

        return MissionResponse.from(savedMission, region);
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
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var region = regionReader.findOne(request.regionId());
        var mission = missionReader.findOne(missionId);

        var requestMission = request.toEntity(missionCategory);
        requestMission.validRangeOfMissionTime(modifiedDateTime);

        mission.update(requestMission);
        return MissionResponse.from(mission, region);
    }

    @Transactional
    public MissionResponse extendMission(
            Long missionId,
            MissionUpdateServiceRequest request,
            LocalDateTime dateTime
    ) {
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var region = regionReader.findOne(request.regionId());
        var mission = missionReader.findOne(missionId);

        var requestExtendMission = request.toEntity(missionCategory);
        requestExtendMission.validRangeOfMissionTime(dateTime);

        mission.extend(requestExtendMission);

        return MissionResponse.from(mission, region);
    }

    private void deleteUserBookMarkByMissionId(
            Long missionId
    ) {
        var userBookMarks = missionBookmarkRepository.findByMissionId(missionId);
        var userBookMarkIds = userBookMarks.stream()
                .map(MissionBookmark::getId)
                .toList();

        missionBookmarkRepository.deleteByIdIn(userBookMarkIds);
    }
}
