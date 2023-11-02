package com.sixheroes.onedayherodomain.missionmatch.repository;

import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionMatchRepository extends JpaRepository<MissionMatch, Long> {

    Optional<MissionMatch> findByMissionId(Long missionId);
}
