package com.sixheroes.onedayherodomain.mission.repository;

import com.sixheroes.onedayherodomain.mission.MissionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionCategoryRepository extends JpaRepository<MissionCategory, Long> {
}
