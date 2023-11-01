package com.sixheroes.onedayheroquerydsl.mission;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayheroquerydsl.mission.response.MissionQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.sixheroes.onedayherodomain.mission.QMission.mission;
import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
import static com.sixheroes.onedayherodomain.region.QRegion.region;

@Repository
@RequiredArgsConstructor
public class MissionQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<Mission> search(Long categoryId, Long regionId, LocalDate findDate) {
        return null;
    }

    public List<Mission> findByCategoryId(Long categoryId) {
        return queryFactory.select(mission)
                .from(mission)
                .where(mission.missionCategory.id.eq(categoryId))
                .fetch();
    }

    public Optional<MissionQueryResponse> fetchOne(Long missionId) {
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
}
