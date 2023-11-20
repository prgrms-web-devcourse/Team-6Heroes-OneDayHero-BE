package com.sixheroes.onedayheroapplication.mission.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroapplication.mission.repository.request.MissionFindFilterQueryRequest;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionProgressQueryResponse;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionQueryResponse;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.sixheroes.onedayherodomain.mission.QMission.mission;
import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
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
                                mission.missionStatus
                        ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(region.id.eq(mission.regionId))
                .where(userIdEq(request.userId()),
                        missionCategoryIdsIn(request.missionCategoryIds()),
                        regionIdsIn(request.regionIds()),
                        missionDatesIn(request.missionDates()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
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
                        mission.missionStatus
                ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(mission.regionId.eq(region.id))
                .where(userIdEq(userId), missionStatusIsProgress())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(mission.createdAt.asc())
                .fetch();
    }

    private BooleanExpression missionStatusIsProgress() {
        return mission.missionStatus.notIn(MissionStatus.MISSION_COMPLETED, MissionStatus.EXPIRED);
    }

    public Optional<MissionQueryResponse> fetchOne(
            Long missionId
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
                        mission.missionStatus
                ))
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(region.id.eq(mission.regionId))
                .where(mission.id.eq(missionId))
                .fetchOne();

        return Optional.ofNullable(queryResult);
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
