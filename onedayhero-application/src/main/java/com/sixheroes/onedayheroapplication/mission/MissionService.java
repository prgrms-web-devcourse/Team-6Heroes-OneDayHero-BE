package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.global.s3.S3ImageDirectoryProperties;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageUploadService;
import com.sixheroes.onedayheroapplication.global.util.SliceResultConverter;
import com.sixheroes.onedayheroapplication.mission.mapper.MissionImageMapper;
import com.sixheroes.onedayheroapplication.mission.repository.MissionQueryRepository;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionProgressQueryResponse;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionQueryResponse;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionExtendServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionFindFilterServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionIdResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionProgressResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionImageRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionService {

    private final MissionCategoryReader missionCategoryReader;
    private final MissionReader missionReader;
    private final MissionRepository missionRepository;
    private final MissionImageRepository missionImageRepository;
    private final MissionBookmarkRepository missionBookmarkRepository;
    private final MissionQueryRepository missionQueryRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDirectoryProperties directoryProperties;

    @Transactional
    public MissionIdResponse createMission(
            MissionCreateServiceRequest request,
            LocalDateTime serverTime
    ) {
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var mission = request.toEntity(missionCategory, serverTime);

        var imageResponse = s3ImageUploadService.uploadImages(request.imageFiles(), directoryProperties.getMissionDir());

        var missionImages = imageResponse.stream()
                .map(MissionImageMapper::createMissionImage)
                .toList();

        mission.addMissionImages(missionImages);
        var savedMission = missionRepository.save(mission);

        return MissionIdResponse.from(savedMission.getId());
    }

    public MissionResponse findOne(
            Long missionId
    ) {
        var missionQueryResponse = missionReader.fetchFindOne(missionId);
        var missionImages = missionImageRepository.findByMission_Id(missionId);
        return MissionResponse.from(missionQueryResponse, missionImages);
    }

    public Slice<MissionResponse> findAllByDynamicCondition(
            Pageable pageable,
            MissionFindFilterServiceRequest request
    ) {
        var missionQueryResponses = missionQueryRepository.findByDynamicCondition(pageable, request.toQuery());
        List<MissionResponse> result = makeMissionResponseWithImages(missionQueryResponses);

        return SliceResultConverter.consume(result, pageable);
    }

    public Slice<MissionProgressResponse> findProgressMission(
            Pageable pageable,
            Long userId
    ) {
        var sliceMissionProgressQueryResponses = missionQueryRepository.findProgressMissionByUserId(pageable, userId);
        var missionProgressResponses = makeProgressMissionResponseWithImages(sliceMissionProgressQueryResponses);

        return SliceResultConverter.consume(missionProgressResponses, pageable);
    }

    @Transactional
    public MissionIdResponse updateMission(
            Long missionId,
            MissionUpdateServiceRequest request,
            LocalDateTime serverTime
    ) {
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var mission = missionReader.findOne(missionId);

        var requestMission = request.toEntity(missionCategory, serverTime);
        mission.update(requestMission, request.citizenId());

        var imageResponse = s3ImageUploadService.uploadImages(request.imageFiles(), directoryProperties.getMissionDir());
        var missionImages = imageResponse.stream()
                .map(MissionImageMapper::createMissionImage)
                .toList();

        mission.addMissionImages(missionImages);

        return MissionIdResponse.from(mission.getId());
    }

    @Transactional
    public MissionIdResponse extendMission(
            Long missionId,
            MissionExtendServiceRequest request,
            LocalDateTime serverTime
    ) {
        var mission = missionReader.findOne(missionId);
        var requestExtendMission = request.toVo(mission.getMissionInfo(), serverTime);

        mission.extend(requestExtendMission, request.citizenId());

        return MissionIdResponse.from(mission.getId());
    }

    public MissionIdResponse completeMission(
            Long missionId,
            Long userId
    ) {
        var mission = missionReader.findOne(missionId);
        mission.complete(userId);

        return MissionIdResponse.from(mission.getId());
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

    private List<MissionResponse> makeMissionResponseWithImages(List<MissionQueryResponse> missionQueryResponses) {
        return missionQueryResponses.stream()
                .map(response -> {
                    var missionImages = missionImageRepository.findByMission_Id(response.id());
                    return MissionResponse.from(response, missionImages);
                }).toList();
    }

    private List<MissionProgressResponse> makeProgressMissionResponseWithImages(List<MissionProgressQueryResponse> sliceMissionProgressQueryResponses) {
        return sliceMissionProgressQueryResponses.stream()
                .map((queryResponse) -> {
                    var missionImages = missionImageRepository.findByMission_Id(queryResponse.id());
                    var thumbNailPath = missionImages.isEmpty() ? null : missionImages.get(0).getPath();
                    return MissionProgressResponse.from(queryResponse, thumbNailPath);
                }).toList();
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
