package com.sixheroes.onedayheroquerydsl.mission;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayheroquerydsl.mission.request.MissionFindFilterQueryRequest;
import com.sixheroes.onedayheroquerydsl.mission.response.MissionQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
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

    public Slice<MissionQueryResponse> findByDynamicCondition(
            Pageable pageable,
            MissionFindFilterQueryRequest request
    ) {
        var content = queryFactory.select(
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
                        missionCategoryIdsEq(request.missionCategoryIds()),
                        regionIdsEq(request.regionIds()),
                        missionDatesEq(request.missionDates()))
                .fetch();

        log.debug("query Size : {}", content.size());

        var totalCount = queryFactory.select(mission.count())
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(region.id.eq(mission.regionId))
                .where(userIdEq(request.userId()),
                        missionCategoryIdsEq(request.missionCategoryIds()),
                        regionIdsEq(request.regionIds()),
                        missionDatesEq(request.missionDates())
                );

        log.debug("total Count : {}", totalCount.fetchOne());

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    public List<Mission> findByCategoryId(
            Long categoryId
    ) {
        return queryFactory.select(mission)
                .from(mission)
                .where(mission.missionCategory.id.eq(categoryId))
                .fetch();
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

    private BooleanBuilder missionCategoryIdsEq(List<Long> missionCategories) {
        var booleanBuilder = new BooleanBuilder();
        for (Long categoryId : missionCategories) {
            booleanBuilder.or(mission.missionCategory.id.eq(categoryId));
        }
        return booleanBuilder;
    }

    private BooleanBuilder regionIdsEq(List<Long> regionIds) {
        var booleanBuilder = new BooleanBuilder();
        for (Long regionId : regionIds) {
            booleanBuilder.or(mission.regionId.eq(regionId));
        }
        return booleanBuilder;
    }

    private BooleanBuilder missionDatesEq(List<LocalDate> missionDates) {
        var booleanBuilder = new BooleanBuilder();
        for (LocalDate missionDate : missionDates) {
            booleanBuilder.or(mission.missionInfo.missionDate.eq(missionDate));
        }
        return booleanBuilder;
    }
}
