package com.sixheroes.onedayheroapplication.missionproposal;

import com.sixheroes.onedayheroapplication.mission.MissionReader;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalCreateEvent;
import com.sixheroes.onedayheroapplication.missionproposal.repository.MissionProposalQueryRepository;
import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalApproveResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalCreateResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalRejectResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalResponses;
import com.sixheroes.onedayheroapplication.user.UserReader;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionProposalService {

    private final MissionProposalRepository missionProposalRepository;
    private final MissionProposalQueryRepository missionProposalQueryRepository;

    private final MissionReader missionReader;
    private final MissionProposalReader missionProposalReader;
    private final UserReader userReader;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public MissionProposalCreateResponse createMissionProposal(
            Long userId,
            MissionProposalCreateServiceRequest request
    ) {
        validMission(request.missionId(), userId);
        validHero(request.heroId());

        var missionProposal = request.toEntity();
        var savedMissionProposal = missionProposalRepository.save(missionProposal);

        var missionProposalEvent = MissionProposalCreateEvent.from(missionProposal);
        applicationEventPublisher.publishEvent(missionProposalEvent);

        return MissionProposalCreateResponse.from(savedMissionProposal);
    }

    @Transactional
    public MissionProposalApproveResponse approveMissionProposal(
            Long userId,
            Long missionProposalId
    ) {
        var missionProposal = missionProposalReader.findOne(missionProposalId);

        var missionId = missionProposal.getMissionId();
        validMission(missionId);

        missionProposal.changeMissionProposalStatusApprove(userId);

        return MissionProposalApproveResponse.from(missionProposal);
    }

    @Transactional
    public MissionProposalRejectResponse rejectMissionProposal(
            Long userId,
            Long missionProposalId
    ) {
        var missionProposal = missionProposalReader.findOne(missionProposalId);

        var missionId = missionProposal.getMissionId();
        validMission(missionId);

        missionProposal.changeMissionProposalStatusReject(userId);

        return MissionProposalRejectResponse.from(missionProposal);
    }

    public MissionProposalResponses findMissionProposal(
            Long heroId,
            Pageable pageable
    ) {
        var slice = missionProposalQueryRepository.findByHeroIdAndPageable(heroId, pageable);
        return MissionProposalResponses.from(slice);
    }

    private void validMission(
            Long missionId,
            Long userId
    ) {
        var mission = missionReader.findOne(missionId);

        mission.validMissionProposalPossible(userId);
    }

    private void validMission(
            Long missionId
    ) {
        var mission = missionReader.findOne(missionId);

        mission.validMissionProposalChangeStatus();
    }

    private void validHero(
            Long heroId
    ) {
        var hero = userReader.findOne(heroId);

        hero.validPossibleMissionProposal();
    }
}
