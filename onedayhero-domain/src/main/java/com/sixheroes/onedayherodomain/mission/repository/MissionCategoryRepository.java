package com.sixheroes.onedayherodomain.mission.repository;

import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.repository.dto.MissionCategoryDto;
import com.sixheroes.onedayherodomain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MissionCategoryRepository extends JpaRepository<MissionCategory, Long> {

    @Query("""
        select new com.sixheroes.onedayherodomain.mission.repository.dto.MissionCategoryDto(
            umc.user.id, mc.missionCategoryCode, mc.name
        )
        from MissionCategory mc
        join UserMissionCategory umc on mc.id = umc.missionCategoryId
        where umc.user in :users
    """)
    List<MissionCategoryDto> findMissionCategoriesByUser(@Param("users") List<User> users);
}
