package com.sixheroes.onedayheroapplication.mission.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroapplication.mission.repository.request.MissionFindFilterQueryRequest;
import com.sixheroes.onedayheroapplication.mission.repository.response.*;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatchStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.sixheroes.onedayherodomain.mission.QMission.mission;
import static com.sixheroes.onedayherodomain.mission.QMissionBookmark.missionBookmark;
import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
import static com.sixheroes.onedayherodomain.missionmatch.QMissionMatch.missionMatch;
import static com.sixheroes.onedayherodomain.region.QRegion.region;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MissionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<MissionQueryResponse> findByDynamicCondition(
            Pageable pageable,
            MissionFindFilterQueryRequest request
    ) {
        return queryFactory.select(
                        Projections.constructor(MissionQueryResponse.class,
                                mission.id,
                                mission.missionCategory.id,
                                mission.missionCategory.missionCategoryCode,
                                mission.missionCategory.name,
                                mission.citizenId,
                                region.id,
                                region.si,
                                region.gu,
                                region.dong,
                                mission.location,
                                mission.missionInfo.title,
                                mission.missionInfo.content,
                                mission.missionInfo.missionDate,
                                mission.missionInfo.startTime,
                                mission.missionInfo.endTime,
                                mission.missionInfo.deadlineTime,
                                mission.missionInfo.price,
                                mission.bookmarkCount,
                                mission.missionStatus,
                                missionBookmark.id
                        ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(region.id.eq(mission.regionId))
                .leftJoin(missionBookmark)
                .on(missionBookmark.mission.id.eq(mission.id), missionBookmark.userId.eq(request.userId()))
                .where(missionCategoryIdsIn(request.missionCategoryIds()),
                        regionIdsIn(request.regionIds()),
                        missionDatesIn(request.missionDates()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L)
                .orderBy(mission.createdAt.asc())
                .fetch();
    }

    public List<MissionProgressQueryResponse> findProgressMissionByUserId(
            Pageable pageable,
            Long userId
    ) {
        return queryFactory.select(Projections.constructor(MissionProgressQueryResponse.class,
                        mission.id,
                        mission.missionInfo.title,
                        mission.missionCategory.id,
                        mission.missionCategory.missionCategoryCode,
                        mission.missionCategory.name,
                        region.si,
                        region.gu,
                        region.dong,
                        mission.missionInfo.missionDate,
                        mission.bookmarkCount,
                        mission.missionStatus,
                        missionBookmark.id
                ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(mission.regionId.eq(region.id))
                .leftJoin(missionBookmark)
                .on(missionBookmark.mission.id.eq(mission.id), missionBookmark.userId.eq(userId))
                .where(userIdEq(userId), missionStatusIsProgress())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L)
                .orderBy(mission.createdAt.desc())
                .fetch();
    }

    public Optional<MissionQueryResponse> fetchOne(
            Long missionId,
            Long userId
    ) {
        var queryResult = queryFactory.select(Projections.constructor(MissionQueryResponse.class,
                        mission.id,
                        mission.missionCategory.id,
                        mission.missionCategory.missionCategoryCode,
                        mission.missionCategory.name,
                        mission.citizenId,
                        region.id,
                        region.si,
                        region.gu,
                        region.dong,
                        mission.location,
                        mission.missionInfo.title,
                        mission.missionInfo.content,
                        mission.missionInfo.missionDate,
                        mission.missionInfo.startTime,
                        mission.missionInfo.endTime,
                        mission.missionInfo.deadlineTime,
                        mission.missionInfo.price,
                        mission.bookmarkCount,
                        mission.missionStatus,
                        missionBookmark.id
                ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(region.id.eq(mission.regionId))
                .leftJoin(missionBookmark)
                .on(missionBookmark.mission.id.eq(mission.id), missionBookmark.userId.eq(userId))
                .where(mission.id.eq(missionId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
    }

    public List<MissionCompletedQueryResponse> findCompletedMissionByUserId(
            Pageable pageable,
            Long userId
    ) {
        return queryFactory.select(Projections.constructor(MissionCompletedQueryResponse.class,
                        mission.id,
                        mission.missionInfo.title,
                        mission.missionCategory.id,
                        mission.missionCategory.missionCategoryCode,
                        mission.missionCategory.name,
                        region.si,
                        region.gu,
                        region.dong,
                        mission.missionInfo.missionDate,
                        mission.bookmarkCount,
                        mission.missionStatus,
                        missionBookmark.id
                ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(mission.regionId.eq(region.id))
                .leftJoin(missionBookmark)
                .on(missionBookmark.mission.id.eq(mission.id), missionBookmark.userId.eq(userId))
                .where(userIdEq(userId), missionStatusIsCompleted())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L)
                .orderBy(mission.missionInfo.missionDate.desc())
                .fetch();
    }

    public List<MissionMatchingQueryResponse> findMissionMatchingResponses(
            Long userId
    ) {
        return queryFactory.select(Projections.constructor(MissionMatchingQueryResponse.class,
                        mission.id,
                        mission.missionInfo.title,
                        mission.missionInfo.price,
                        mission.missionInfo.missionDate,
                        mission.missionInfo.startTime,
                        mission.missionInfo.endTime,
                        mission.createdAt,
                        mission.missionCategory.id,
                        mission.missionCategory.missionCategoryCode,
                        mission.missionCategory.name,
                        region.id,
                        region.si,
                        region.gu,
                        region.dong,
                        mission.bookmarkCount,
                        mission.missionStatus,
                        missionBookmark.id
                ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(mission.regionId.eq(region.id))
                .leftJoin(missionBookmark)
                .on(missionBookmark.mission.id.eq(mission.id), missionBookmark.userId.eq(userId))
                .where(userIdEq(userId), missionStatusIsMatching())
                .orderBy(mission.missionInfo.missionDate.desc())
                .fetch();
    }

    public Optional<MissionCompletedEventQueryResponse> findMissionCompletedEvent(
            Long missionId
    ) {
        var missionCompletedEventQueryResponse = queryFactory.select(Projections.constructor(MissionCompletedEventQueryResponse.class,
                        missionMatch.heroId, mission.id, mission.missionInfo.title
                ))
                .from(mission)
                .join(missionMatch)
                .on(mission.id.eq(missionMatch.missionId))
                .where(missionIdEq(missionId).and(missionMatchStatusMatched()))
                .fetchOne();

        return Optional.ofNullable(missionCompletedEventQueryResponse);
    }

    private BooleanExpression missionMatchStatusMatched() {
        return missionMatch.missionMatchStatus.eq(MissionMatchStatus.MATCHED);
    }

    private BooleanBuilder missionStatusIsMatching() {
        return new BooleanBuilder(mission.missionStatus.eq(MissionStatus.MATCHING));
    }

    private BooleanExpression missionStatusIsProgress() {
        return mission.missionStatus.notIn(MissionStatus.MISSION_COMPLETED, MissionStatus.EXPIRED);
    }

    private BooleanExpression missionStatusIsCompleted() {
        return mission.missionStatus.eq(MissionStatus.MISSION_COMPLETED);
    }

    private BooleanBuilder missionIdEq(Long missionId) {
        return new BooleanBuilder(mission.id.eq(missionId));
    }


    private BooleanBuilder userIdEq(Long userId) {
        return new BooleanBuilder(mission.citizenId.eq(userId));
    }

    private BooleanBuilder missionCategoryIdsIn(List<Long> missionCategories) {
        if (missionCategories == null || missionCategories.isEmpty()) {
            return null;
        }

        return new BooleanBuilder(mission.missionCategory.id.in(missionCategories));
    }

    private BooleanBuilder regionIdsIn(List<Long> regionIds) {
        if (regionIds == null || regionIds.isEmpty()) {
            return null;
        }

        return new BooleanBuilder(mission.regionId.in(regionIds));
    }

    private BooleanBuilder missionDatesIn(List<LocalDate> missionDates) {
        if (missionDates == null || missionDates.isEmpty()) {
            return null;
        }

        return new BooleanBuilder(mission.missionInfo.missionDate.in(missionDates));
    }
}
