package com.sixheroes.onedayherodomain.mission.repository;

import com.sixheroes.onedayherodomain.mission.MissionImage;
import com.sixheroes.onedayherodomain.mission.repository.dto.MissionImageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MissionImageRepository extends JpaRepository<MissionImage, Long> {

    List<MissionImage> findByMission_Id(Long missionId);

    List<MissionImage> findByIdIn(List<Long> ids);

    @Query("""  
        select 
            new com.sixheroes.onedayherodomain.mission.repository.dto.MissionImageResponse(
                m.id, mi.path
            )
        from MissionImage mi
        join Mission m on mi.mission.id = m.id
        where mi.mission.id in :missionIds
    """)
    List<MissionImageResponse> findByMissionIdIn(@Param("missionIds") List<Long> missionIds);
}
