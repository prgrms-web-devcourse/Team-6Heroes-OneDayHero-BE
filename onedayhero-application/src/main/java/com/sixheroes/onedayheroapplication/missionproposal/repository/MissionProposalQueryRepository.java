package com.sixheroes.onedayheroapplication.missionproposal.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroapplication.missionproposal.repository.dto.MissionProposalQueryDto;
import com.sixheroes.onedayherodomain.missionproposal.MissionProposalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sixheroes.onedayherodomain.mission.QMission.mission;
import static com.sixheroes.onedayherodomain.mission.QMissionBookmark.missionBookmark;
import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
import static com.sixheroes.onedayherodomain.missionproposal.QMissionProposal.missionProposal;
import static com.sixheroes.onedayherodomain.region.QRegion.region;

@RequiredArgsConstructor
@Repository
public class MissionProposalQueryRepository {

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
    public Slice<MissionProposalQueryDto> findByHeroIdAndPageable(
            Long heroId,
            Pageable pageable
    ) {
        var pageSize = pageable.getPageSize();
        List<MissionProposalQueryDto> content = jpaQueryFactory.select(Projections.constructor(MissionProposalQueryDto.class,
                    missionProposal.id,
                    mission.id,
                    mission.citizenId,
                    mission.missionInfo.title,
                    mission.missionInfo.price,
                    mission.missionInfo.missionDate,
                    mission.missionInfo.startTime,
                    mission.missionInfo.endTime,
                    mission.createdAt,
                    mission.missionCategory.id,
                    mission.missionCategory.missionCategoryCode,
                    mission.missionCategory.name,
                    region.si,
                    region.gu,
                    region.dong,
                    mission.bookmarkCount,
                    mission.missionStatus,
                    missionBookmark.id,
                    missionProposal.createdAt
                ))
                .from(missionProposal)
                .join(mission)
                .on(missionProposal.missionId.eq(mission.id))
                .join(mission.missionCategory, missionCategory)
                .join(region)
                .on(mission.regionId.eq(region.id))
                .leftJoin(missionBookmark)
                .on(missionBookmark.mission.id.eq(mission.id), missionBookmark.userId.eq(heroId))
                .where(createBooleanBuilder(heroId))
                .orderBy(mission.priorityStatus.desc(), missionProposal.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageSize + 1L)
                .fetch();

        var hasNext = false;
        if (content.size() > pageSize) {
            hasNext = true;
            content.remove(pageSize);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanBuilder createBooleanBuilder(
            Long heroId
    ) {
        var booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(heroIdEq(heroId))
                .and(missionProposalStatusRequestEq());
    }

    private BooleanExpression heroIdEq(
            Long heroId
    ) {
        if (heroId != null) {
            return missionProposal.heroId.eq(heroId);
        }
        return null;
    }

    private BooleanExpression missionProposalStatusRequestEq() {
        return missionProposal.missionProposalStatus.eq(MissionProposalStatus.PROPOSAL);
    }
}
