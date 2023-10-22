package com.sixheroes.onedayherodomain.achievement.repository;

import com.sixheroes.onedayherodomain.achievement.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}
