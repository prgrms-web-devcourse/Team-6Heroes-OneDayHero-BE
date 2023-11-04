package com.sixheroes.onedayheroquerydsl.mission;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroquerydsl.mission.response.MissionBookmarkMeQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;


import static com.sixheroes.onedayherodomain.mission.QMission.mission;
import static com.sixheroes.onedayherodomain.mission.QMissionBookmark.missionBookmark;
import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
import static com.sixheroes.onedayherodomain.region.QRegion.region;


@Slf4j
@Repository
@RequiredArgsConstructor
public class MissionBookmarkQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<MissionBookmarkMeQueryResponse> me(
            Pageable pageable,
            Long userId
    ) {
        var content = queryFactory
                .select(
                        Projections.constructor(
                                MissionBookmarkMeQueryResponse.class,
                                mission.id,
                                missionBookmark.id,
                                mission.missionStatus,
                                mission.missionInfo.content, //TODO: title
                                mission.bookmarkCount,
                                mission.missionInfo.price,
                                mission.missionInfo.missionDate,
                                mission.missionInfo.startTime,
                                mission.missionInfo.endTime,
                                mission.missionCategory.name, //TODO: categoryId 캐싱 고려
                                region.si, //TODO: regionId 캐싱 고려
                                region.gu,
                                region.dong
                        )
                )
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(missionBookmark).on(mission.id.eq(missionBookmark.mission.id))
                .join(region).on(mission.regionId.eq(region.id))
                .where(missionBookmark.userId.eq(userId))
                .offset(pageable.getOffset()) //TODO: 인덱스 및 NO-OFFSET 적용 고려
                .limit(pageable.getPageSize())
                .fetch();

        var totalCount = queryFactory
                .select(missionBookmark.count())
                .from(mission)
                .join(mission.missionCategory, missionCategory)
                .join(missionBookmark).on(mission.id.eq(missionBookmark.mission.id))
                .join(region).on(mission.regionId.eq(region.id))
                .where(missionBookmark.userId.eq(userId));

        return PageableExecutionUtils.getPage(
                content,
                pageable,
                totalCount::fetchOne
        );
    }
}
