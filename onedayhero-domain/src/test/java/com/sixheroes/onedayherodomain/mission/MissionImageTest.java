package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MissionImageTest {

    @DisplayName("미션 이미지 삭제는 유저가 만든 미션에 대해서만 삭제가 가능하다.")
    @Test
    void deleteMissionImageWithOwn() {
        // given
        var unknownId = 2L;

        var missionCategory = MissionCategory.builder()
                .missionCategoryCode(MissionCategoryCode.MC_001)
                .name("서빙")
                .build();

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

        // when & then
        Assertions.assertThatThrownBy(() -> missionImage.validOwn(unknownId))
                .isInstanceOf(BusinessException.class);
    }
}
