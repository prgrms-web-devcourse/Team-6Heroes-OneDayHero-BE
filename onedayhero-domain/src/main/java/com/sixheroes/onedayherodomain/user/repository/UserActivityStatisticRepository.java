package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.UserActivityStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityStatisticRepository extends JpaRepository<UserActivityStatistic, Long> {
}
