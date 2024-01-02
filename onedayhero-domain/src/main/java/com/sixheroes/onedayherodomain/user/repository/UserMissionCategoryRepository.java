package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.mission.repository.dto.MissionCategoryDto;
import com.sixheroes.onedayherodomain.user.UserMissionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMissionCategoryRepository extends JpaRepository<UserMissionCategory, Long> {

    @Query("""
        select new com.sixheroes.onedayherodomain.mission.repository.dto.MissionCategoryDto(
            umc.user.id, mc.missionCategoryCode, mc.name
        )
        from UserMissionCategory umc
        join MissionCategory mc on umc.missionCategoryId = mc.id
        where umc.user.id in :userIds
    """)
    List<MissionCategoryDto> findByUsers(
        @Param("userIds") List<Long> userIds
    );
}
