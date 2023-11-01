package com.sixheroes.onedayheroquerydsl.mission;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayherodomain.mission.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.sixheroes.onedayherodomain.mission.QMission.mission;

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
}
