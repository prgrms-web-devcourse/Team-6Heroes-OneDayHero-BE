package com.sixheroes.onedayheroapplication.mission;


import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCancelResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayheroquerydsl.mission.MissionBookmarkQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MissionBookmarkService {

    private final MissionBookmarkRepository missionBookmarkRepository;
    private final MissionBookmarkReader missionBookmarkReader;
    private final MissionBookmarkQueryRepository missionBookmarkQueryRepository;
    private final MissionReader missionReader;

    @Transactional(readOnly = true)
    public MissionBookmarkMeViewResponse me(
            Pageable pageable,
            Long userId
    ) {
        var responses = missionBookmarkQueryRepository.me(
                pageable,
                userId
        );

        return MissionBookmarkMeViewResponse.of(
                userId,
                responses
        );
    }

    public MissionBookmarkCreateResponse createMissionBookmark(MissionBookmarkCreateServiceRequest request) {
        var mission = missionReader.findOne(request.missionId());
        var missionBookmark = MissionBookmark.builder()
                .mission(mission)
                .userId(request.userId())
                .build();

        validateMissionBookmarkIsAlreadyExist(
                mission.getId(),
                request.userId()
        );

        var savedMissionBookmark = missionBookmarkRepository.save(missionBookmark);
        mission.addBookmarkCount();

        return new MissionBookmarkCreateResponse(savedMissionBookmark);
    }

    public MissionBookmarkCancelResponse cancelMissionBookmark(MissionBookmarkCancelServiceRequest request) {
        var mission = missionReader.findOne(request.missionId());
        var findMissionBookmark = missionBookmarkReader.findByMissionIdAndUserId(
                request.missionId(),
                request.userId()
        );

        missionBookmarkRepository.delete(findMissionBookmark);
        mission.subBookmarkCount();

        return MissionBookmarkCancelResponse.builder()
                .missionBookmarkId(findMissionBookmark.getId())
                .missionId(mission.getId())
                .userId(findMissionBookmark.getUserId())
                .build();
    }

    private void validateMissionBookmarkIsAlreadyExist(
            Long missionId,
            Long userId) {

        if (missionBookmarkRepository.existsByMissionIdAndUserId(
                missionId,
                userId)) {
            log.debug("이미 해당 미션에 대해 찜 했습니다.");
            throw new IllegalStateException(ErrorCode.T_001.name());
        }
    }
}
