package com.sixheroes.onedayherodomain.missionmatch.repository;

import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatchStatus;
import com.sixheroes.onedayherodomain.missionmatch.repository.dto.MissionMatchEventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MissionMatchRepository extends JpaRepository<MissionMatch, Long> {

    Optional<MissionMatch> findByMissionIdAndMissionMatchStatus(Long missionId, MissionMatchStatus missionMatchStatus);

    @Query("""
        select 
            new com.sixheroes.onedayherodomain.missionmatch.repository.dto.MissionMatchEventDto(
                mm.heroId, u.userBasicInfo.nickname, m.id, m.missionInfo.title
            )
        from MissionMatch mm
        join Mission m on m.id = mm.missionId
        join User u on u.id = m.citizenId
        where mm.id = :missionMatchId
    """)
    Optional<MissionMatchEventDto> findMissionMatchEvenSendCitizenById(
        @Param("missionMatchId") Long missionMatchId
    );

    @Query("""
        select 
            new com.sixheroes.onedayherodomain.missionmatch.repository.dto.MissionMatchEventDto(
                m.citizenId, u.userBasicInfo.nickname, m.id, m.missionInfo.title
            )
        from MissionMatch mm
        join Mission m on m.id = mm.missionId
        join User u on u.id = mm.heroId
        where mm.id = :missionMatchId
    """)
    Optional<MissionMatchEventDto> findMissionMatchEventSendHeroById(
        @Param("missionMatchId") Long missionMatchId
    );
}
