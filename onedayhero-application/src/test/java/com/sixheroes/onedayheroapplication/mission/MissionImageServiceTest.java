package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayherodomain.mission.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Transactional
public class MissionImageServiceTest extends IntegrationApplicationTest {

    @DisplayName("미션의 개별 이미지를 삭제 할 수 있다.")
    @Test
    void deleteMissionImage() {
        // given
        var citizenId = 1L;

        var missionCategory = MissionCategory.builder()
                .missionCategoryCode(MissionCategoryCode.MC_001)
                .name("서빙")
                .build();

        missionCategoryRepository.save(missionCategory);

        var mission = Mission.builder()
                .missionCategory(missionCategory)
                .citizenId(1L)
                .regionId(1L)
                .location(Mission.createPoint(127.3324, 36.1807))
                .missionInfo(MissionInfo.builder()
                        .missionDate(LocalDate.of(2023, 11, 25))
                        .startTime(LocalTime.of(9, 0, 0))
                        .endTime(LocalTime.of(14, 0, 0))
                        .deadlineTime(LocalDateTime.of(
                                LocalDate.of(2023, 11, 25),
                                LocalTime.of(9, 0, 0)
                        ))
                        .serverTime(LocalDateTime.of(
                                LocalDate.of(2023, 10, 18),
                                LocalTime.MIDNIGHT
                        ))
                        .title("벌레를 잡아주세요!")
                        .content("이상하게 생긴 벌레가 있어요 ㅠㅠㅠ")
                        .price(5500)
                        .build())
                .bookmarkCount(0)
                .missionStatus(MissionStatus.MATCHING)
                .build();

        var missionImage = MissionImage.builder()
                .originalName("image.jpeg")
                .uniqueName("aabcerefg12312.jpg")
                .path("s3://path")
                .mission(mission)
                .build();

        mission.addMissionImages(List.of(missionImage));
        var savedMission = missionRepository.save(mission);

        // when
        var missionImages = missionImageRepository.findByMission_Id(savedMission.getId());

        var missionImageId = missionImages.get(0).getId();
        missionImageService.deleteImage(missionImageId, citizenId);

        var findMissionImage = missionImageRepository.findById(missionImageId);

        // then
        Assertions.assertThat(findMissionImage).isEmpty();
    }
}
