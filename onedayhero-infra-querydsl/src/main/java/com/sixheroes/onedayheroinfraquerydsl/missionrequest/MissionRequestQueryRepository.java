package com.sixheroes.onedayheroinfraquerydsl.missionrequest;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.missionrequest.MissionRequestStatus;
import com.sixheroes.onedayheroinfraquerydsl.missionrequest.dto.MissionRequestQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sixheroes.onedayherodomain.mission.QMission.mission;
import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
import static com.sixheroes.onedayherodomain.missionrequest.QMissionRequest.missionRequest;
import static com.sixheroes.onedayherodomain.region.QRegion.region;

@RequiredArgsConstructor
@Repository
public class MissionRequestQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 제안 받은 미션 조회
     * mission request -> 히어로 아이디 = heroId, 미션 제안 상태 = REQUEST
     * mission -> bookmark count
     * missionInfo(value) -> missionDate, startTime, endTime, price, createdAt
     * mission category -> code, name
     * region -> si, gu, dong
     * 정렬 : 미션 상태 매칭 중인 상태가 제일 위에 | 나머지 상태 (매칭 완료, 매칭 완료, 마감된 미션)는 제일 아래
     * createdAt 최신 순으로 정렬
     */
    public Slice<MissionRequestQueryDto> findByHeroIdAndPageable(
            Long heroId,
            Pageable pageable
    ) {
        var pageSize = pageable.getPageSize();
        List<MissionRequestQueryDto> content = jpaQueryFactory.select(Projections.constructor(MissionRequestQueryDto.class,
                        missionRequest.id,
                        mission.id,
                        mission.missionStatus,
                        mission.bookmarkCount,
                        mission.createdAt,
                        mission.missionInfo.missionDate,
                        mission.missionInfo.startTime,
                        mission.missionInfo.endTime,
                        mission.missionInfo.price,
                        missionCategory.missionCategoryCode,
                        missionCategory.name,
                        region.si,
                        region.gu,
                        region.dong
                ))
                .from(missionRequest)
                .join(mission)
                .on(missionRequest.missionId.eq(mission.id))
                .join(missionCategory)
                .on(mission.missionCategory.eq(missionCategory))
                .join(region)
                .on(mission.regionId.eq(region.id))
                .where(createBooleanBuilder(heroId))
                .orderBy(buildMissionStatusCaseBuilder().desc(), missionRequest.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        var hasNext = false;
        if (content.size() > pageSize) {
            hasNext = true;
            content.remove(pageSize);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private NumberExpression<Integer> buildMissionStatusCaseBuilder() {
        return new CaseBuilder()
            .when(mission.missionStatus.eq(MissionStatus.EXPIRED))
            .then(0)
            .when(mission.missionStatus.eq(MissionStatus.MISSION_COMPLETED))
            .then(1)
            .when(mission.missionStatus.eq(MissionStatus.MATCHING_COMPLETED))
            .then(2)
            .otherwise(3);
    }

    private BooleanBuilder createBooleanBuilder(
        Long heroId
    ) {
        var booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(heroIdEq(heroId))
                .and(missionRequestStatusRequestEq());
    }

    private BooleanExpression heroIdEq(
        Long heroId
    ) {
        if (heroId != null) {
            return missionRequest.heroId.eq(heroId);
        }
        return null;
    }

    private BooleanExpression missionRequestStatusRequestEq() {
        return missionRequest.missionRequestStatus.eq(MissionRequestStatus.REQUEST);
    }
}
