package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.global.s3.S3ImageDirectoryProperties;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageUploadService;
import com.sixheroes.onedayheroapplication.global.util.SliceResultConverter;
import com.sixheroes.onedayheroapplication.main.request.UserPositionServiceRequest;
import com.sixheroes.onedayheroapplication.mission.converter.PointConverter;
import com.sixheroes.onedayheroapplication.mission.event.dto.MissionCompletedEvent;
import com.sixheroes.onedayheroapplication.mission.mapper.MissionImageMapper;
import com.sixheroes.onedayheroapplication.mission.repository.MissionQueryRepository;
import com.sixheroes.onedayheroapplication.mission.repository.response.*;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionExtendServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionFindFilterServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.*;
import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalReader;
import com.sixheroes.onedayheroapplication.region.RegionReader;
import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionImageRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.mission.repository.dto.MissionImageQueryResponse;
import com.sixheroes.onedayherodomain.mission.repository.response.MissionAroundQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionService {

    private static final Integer DISTANCE = 5000;

    private final MissionProposalReader missionProposalReader;
    private final MissionCategoryReader missionCategoryReader;
    private final RegionReader regionReader;
    private final MissionReader missionReader;

    private final MissionRepository missionRepository;
    private final MissionImageRepository missionImageRepository;
    private final MissionBookmarkRepository missionBookmarkRepository;
    private final MissionQueryRepository missionQueryRepository;

    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDirectoryProperties directoryProperties;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public MissionIdResponse createMission(
            MissionCreateServiceRequest request,
            LocalDateTime serverTime
    ) {
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var region = regionReader.findByDong(request.regionName());
        var mission = request.toEntity(missionCategory, region.getId(), serverTime);

        var imageResponse = s3ImageUploadService.uploadImages(request.imageFiles(), directoryProperties.getMissionDir());

        var missionImages = imageResponse.stream()
                .map(MissionImageMapper::createMissionImage)
                .toList();

        mission.addMissionImages(missionImages);
        var savedMission = missionRepository.save(mission);

        return MissionIdResponse.from(savedMission.getId());
    }

    public MissionResponse findOne(
            Long userId,
            Long missionId
    ) {
        var missionQueryResponse = missionReader.fetchFindOne(missionId, userId);
        var missionImages = missionImageRepository.findByMission_Id(missionId);
        var isBookmarked = missionQueryResponse.bookmarkId() != null;

        return MissionResponse.from(missionQueryResponse, missionImages, isBookmarked);
    }

    public Slice<MissionResponse> findAllByDynamicCondition(
            Pageable pageable,
            MissionFindFilterServiceRequest request
    ) {
        var missionQueryResponses = missionQueryRepository.findByDynamicCondition(pageable, request.toQuery());
        var result = makeMissionResponseWithImages(missionQueryResponses);

        return SliceResultConverter.consume(result, pageable);
    }

    public Slice<MissionProgressResponse> findProgressMissions(
            Pageable pageable,
            Long userId
    ) {
        var sliceMissionProgressQueryResponses = missionQueryRepository.findProgressMissionByUserId(pageable, userId);
        var missionProgressResponses = makeProgressMissionResponseWithImages(sliceMissionProgressQueryResponses);

        return SliceResultConverter.consume(missionProgressResponses, pageable);
    }

    public Slice<MissionAroundResponse> findAroundMissions(
            Pageable pageable,
            UserPositionServiceRequest request
    ) {
        var point = PointConverter.pointToString(request.longitude(), request.latitude());
        var aroundMissionQueryResponses = missionRepository.findAroundMissionByLocation(point, DISTANCE, pageable.getPageSize() + 1, pageable.getOffset());
        var aroundMissionResponses = makeAroundMissionResponseWithImage(aroundMissionQueryResponses);

        return SliceResultConverter.consume(aroundMissionResponses, pageable);
    }

    @Transactional
    public MissionIdResponse updateMission(
            Long missionId,
            MissionUpdateServiceRequest request,
            LocalDateTime serverTime
    ) {
        var missionCategory = missionCategoryReader.findOne(request.missionCategoryId());
        var mission = missionReader.findOne(missionId);
        var region = regionReader.findByDong(request.regionName());

        var requestMission = request.toEntity(missionCategory, serverTime, region.getId());
        mission.update(requestMission, request.userId());

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

        mission.extend(requestExtendMission, request.userId());

        return MissionIdResponse.from(mission.getId());
    }

    @Transactional
    public MissionIdResponse completeMission(
            Long missionId,
            Long userId
    ) {
        var mission = missionReader.findOne(missionId);
        mission.complete(userId);

        var missionCompletedEvent = MissionCompletedEvent.from(mission);
        applicationEventPublisher.publishEvent(missionCompletedEvent);

        return MissionIdResponse.from(mission.getId());
    }

    public Slice<MissionCompletedResponse> findCompletedMissionsByUserId(
            Pageable pageable,
            Long userId
    ) {
        var completedMissions = missionQueryRepository.findCompletedMissionByUserId(pageable, userId);
        var missionCompletedResponses = makeCompletedMissionResponseWithImages(completedMissions);

        return SliceResultConverter.consume(missionCompletedResponses, pageable);
    }

    public MissionMatchingResponses findMatchingMissionsByUserId(
            Long userId,
            Long heroId
    ) {
        var matchingMissions = missionQueryRepository.findMissionMatchingResponses(userId);
        var proposedMissions = missionProposalReader.findProposedMissions(heroId);

        var matchingMissionsFiltered = matchingMissions.stream()
            .filter(mission -> proposedMissions.stream()
                .noneMatch(missionProposal -> Objects.equals(mission.id(), missionProposal.getMissionId()))
            )
            .toList();
        var missionMatchingResponses = makeMatchingMissionResponseWithImages(matchingMissionsFiltered);

        return new MissionMatchingResponses(missionMatchingResponses);
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

    private List<MissionResponse> makeMissionResponseWithImages(
            List<MissionQueryResponse> missionQueryResponses
    ) {
        return missionQueryResponses.stream()
                .map(response -> {
                    var missionImages = missionImageRepository.findByMission_Id(response.id());
                    var isBookmarked = response.bookmarkId() != null;
                    return MissionResponse.from(response, missionImages, isBookmarked);
                }).toList();
    }

    private List<MissionAroundResponse> makeAroundMissionResponseWithImage(
            List<MissionAroundQueryResponse> queryResponse
    ) {
        return queryResponse.stream()
                .map(response -> {
                    var missionImages = missionImageRepository.findByMission_Id(response.getId());
                    var thumbNailPath = missionImages.isEmpty() ? null : missionImages.get(0).getPath();
                    return MissionAroundResponse.from(response, thumbNailPath);
                }).collect(Collectors.toList());
    }

    private List<MissionProgressResponse> makeProgressMissionResponseWithImages(
            List<MissionProgressQueryResponse> sliceMissionProgressQueryResponses
    ) {
        return sliceMissionProgressQueryResponses.stream()
                .map(queryResponse -> {
                    var missionImages = missionImageRepository.findByMission_Id(queryResponse.id());
                    var thumbNailPath = missionImages.isEmpty() ? null : missionImages.get(0).getPath();
                    var isBookmarked = queryResponse.bookmarkId() != null;
                    return MissionProgressResponse.from(queryResponse, thumbNailPath, isBookmarked);
                }).collect(Collectors.toList());
    }

    private List<MissionCompletedResponse> makeCompletedMissionResponseWithImages(
            List<MissionCompletedQueryResponse> sliceMissionCompletedQueryResponses
    ) {
        return sliceMissionCompletedQueryResponses.stream()
                .map(queryResponse -> {
                    var missionImages = missionImageRepository.findByMission_Id(queryResponse.id());
                    var thumbNailPath = missionImages.isEmpty() ? null : missionImages.get(0).getPath();
                    var isBookmarked = queryResponse.bookmarkId() != null;
                    return MissionCompletedResponse.from(queryResponse, thumbNailPath, isBookmarked);
                }).collect(Collectors.toList());
    }

    private List<MissionMatchingResponse> makeMatchingMissionResponseWithImages(
            List<MissionMatchingQueryResponse> missionMatchingQueryResponses
    ) {
        var missionIds = missionMatchingQueryResponses.stream()
                .map(MissionMatchingQueryResponse::id)
                .toList();

        var missionImageGroupingByMission = missionImageRepository.findByMissionIdIn(missionIds)
                .stream()
                .collect(Collectors.groupingBy(MissionImageQueryResponse::missionId));

        return missionMatchingQueryResponses.stream()
                .map(queryResponse -> {
                    var missionId = queryResponse.id();
                    var missionImage = Optional.ofNullable(missionImageGroupingByMission.get(missionId))
                            .filter(Predicate.not(List::isEmpty))
                            .map(list -> list.get(0).path())
                            .orElse(null);
                    var isBookmarked = queryResponse.bookmarkId() != null;
                    return MissionMatchingResponse.from(queryResponse, missionImage, isBookmarked);
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
