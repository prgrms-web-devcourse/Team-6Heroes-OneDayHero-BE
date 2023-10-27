package com.sixheroes.onedayherodomain.bookmark.repository;

import com.sixheroes.onedayherodomain.bookmark.UserBookMarkMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookMarkMissionRepository extends JpaRepository<UserBookMarkMission, Long> {

    List<UserBookMarkMission> findByMissionIdAndUserId(Long missionId, Long userId);

    void deleteByIdIn(List<Long> userBookmarkIds);
}