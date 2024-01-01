package com.sixheroes.onedayheroapplication.user.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroapplication.user.repository.dto.HeroRankQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
import static com.sixheroes.onedayherodomain.user.QUser.user;
import static com.sixheroes.onedayherodomain.user.QUserImage.userImage;
import static com.sixheroes.onedayherodomain.user.QUserMissionCategory.userMissionCategory;
import static com.sixheroes.onedayherodomain.user.QUserRegion.userRegion;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<HeroRankQueryResponse> findHeroesRank(
        Long regionId,
        Long missionCategoryId,
        Pageable pageable
    ) {
        var pageSize = pageable.getPageSize();

        return queryFactory.select(
                Projections.constructor(HeroRankQueryResponse.class,
                    missionCategory.name,
                    user.id,
                    user.userBasicInfo.nickname,
                    user.heroScore,
                    userImage.path
                )
            )
            .from(user)
            .join(userRegion)
            .on(userRegion.user.eq(user))
            .join(userMissionCategory)
            .on(userMissionCategory.user.eq(user))
            .leftJoin(userImage)
            .on(userImage.user.eq(user))
            .join(missionCategory)
            .on(missionCategory.id.eq(userMissionCategory.missionCategoryId))
            .where(createBooleanBuilder(regionId, missionCategoryId))
            .orderBy(user.heroScore.desc())
            .offset(pageable.getOffset())
            .limit(pageSize + 1L)
            .fetch();
    }

    private BooleanBuilder createBooleanBuilder(
        Long regionId,
        Long missionCategoryId
    ) {
        return regionIdEq(regionId).and(missionCategoryIdEq(missionCategoryId));
    }

    private BooleanBuilder regionIdEq(
        Long regionId
    ) {
        return new BooleanBuilder(userRegion.regionId.eq(regionId));
    }

    private BooleanBuilder missionCategoryIdEq(
        Long missionCategoryId
    ) {
        return new BooleanBuilder(userMissionCategory.missionCategoryId.eq(missionCategoryId));
    }
}
