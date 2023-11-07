package com.sixheroes.onedayherodomain.mission.repository;

import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionBookmarkRepository extends JpaRepository<MissionBookmark, Long> {

    Optional<MissionBookmark> findByMissionIdAndUserId(Long missionId, Long userId);

    Boolean existsByMissionIdAndUserId(Long missionId, Long userId);

    List<MissionBookmark> findByMissionId(Long missionId);

    void deleteByIdIn(List<Long> userBookmarkIds);
}
