package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayherodomain.mission.repository.MissionImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MissionImageService {

    private final MissionImageReader missionImageReader;
    private final MissionImageRepository missionImageRepository;

    @Transactional
    public void deleteImage(
            Long missionImageId,
            Long userId
    ) {
        var missionImage = missionImageReader.findById(missionImageId);
        missionImage.validOwn(userId);

        missionImageRepository.delete(missionImage);
    }
}
