package com.sixheroes.onedayheroquerydsl.mission;

import com.sixheroes.onedayherodomain.mission.Mission;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface MissionRepositoryCustom {
    Slice<Mission> search(Long categoryId, Long regionId, LocalDate findDate);

    List<Mission> findByCategoryId(Long categoryId);
}
