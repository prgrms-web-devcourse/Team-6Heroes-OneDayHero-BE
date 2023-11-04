package com.sixheroes.onedayheroapplication.mission;


import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCancelResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeLineDto;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
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
        var queryResponses = missionBookmarkQueryRepository.me(
                pageable,
                userId
        );

        var missionBookmarkMeLineDtos = queryResponses.getContent()
                .stream()
                .map(MissionBookmarkMeLineDto::from)
                .toList();

        return new MissionBookmarkMeViewResponse(
                pageable,
                missionBookmarkMeLineDtos,
                queryResponses.hasNext()
        );
    }

    public MissionBookmarkCreateResponse createMissionBookmark(MissionBookmarkCreateServiceRequest request) {
        //TODO : UserReader 를 통한 히어로 유저 존재 검증
        var mission = missionReader.findOne(request.missionId());
        var missionBookmark = MissionBookmark.builder()
                .mission(mission)
                .userId(request.userId())
                .build();

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
                .id(findMissionBookmark.getId())
                .missionId(mission.getId())
                .userId(findMissionBookmark.getUserId())
                .build();
    }
}
