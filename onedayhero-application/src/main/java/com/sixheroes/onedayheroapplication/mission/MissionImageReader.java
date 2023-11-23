package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayherodomain.mission.repository.MissionImageRepository;
import com.sixheroes.onedayherodomain.mission.repository.dto.MissionImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class MissionImageReader {

    private final MissionImageRepository missionImageRepository;

    public Map<Long, List<MissionImageResponse>> findMissionImageByMissionId(
        List<Long> missionId
    ) {
        var missionImages = missionImageRepository.findByMissionIdIn(missionId);

        return missionImages.stream()
            .collect(Collectors.groupingBy(MissionImageResponse::missionId));
    }
}
