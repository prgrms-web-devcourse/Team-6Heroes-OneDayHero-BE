package com.sixheroes.onedayherodomain.missionproposal.repository;

import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import com.sixheroes.onedayherodomain.missionproposal.repository.dto.MissionProposalCreateEventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MissionProposalRepository extends JpaRepository<MissionProposal, Long> {

    @Query("""
        select 
         new com.sixheroes.onedayherodomain.missionproposal.repository.dto.MissionProposalCreateEventDto(
            mp.heroId, u.userBasicInfo.nickname, m.id, m.missionInfo.title
         )
        from MissionProposal mp
        join Mission m on mp.missionId = m.id
        join User u on m.citizenId = u.id
        where mp.id = :missionProposalId
    """)
    Optional<MissionProposalCreateEventDto> findMissionProposalCreateEventDtoById(
        @Param("missionProposalId") Long missionProposalId
    );
}
