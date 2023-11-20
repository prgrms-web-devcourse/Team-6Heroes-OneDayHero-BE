package com.sixheroes.onedayherodomain.mission.repository;

import com.sixheroes.onedayherodomain.mission.MissionImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionImageRepository extends JpaRepository<MissionImage, Long> {

    List<MissionImage> findByMission_Id(Long missionId);

    List<MissionImage> findByIdIn(List<Long> ids);
}
