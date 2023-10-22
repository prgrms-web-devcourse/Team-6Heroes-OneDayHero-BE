package com.sixheroes.onedayherodomain.achievement.repository;

import com.sixheroes.onedayherodomain.achievement.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
}
