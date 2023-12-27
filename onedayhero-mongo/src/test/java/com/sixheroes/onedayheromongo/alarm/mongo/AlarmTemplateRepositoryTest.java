package com.sixheroes.onedayheromongo.alarm.mongo;

import com.sixheroes.onedayheromongo.IntegrationMongoRepositoryTest;
import com.sixheroes.onedayheromongo.alarm.AlarmTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlarmTemplateRepositoryTest extends IntegrationMongoRepositoryTest {

    @DisplayName("알람 타입으로 알림 템플릿을 찾는다")
    @Test
    void findByAlarmType() {
        // given
        var alarmType = "MISSION_PROPOSAL_CREATE";
        var title = "제안받은 미션을 확인해보세요.";
        var content = "${citizenNickname} 님께서 '${missionTitle}' 미션을 제안하셨습니다.";
        var alarmTemplate = AlarmTemplate.builder()
            .alarmType(alarmType)
            .title(title)
            .content(content)
            .build();
        alarmTemplateRepository.save(alarmTemplate);

        // when
        var findAlarmType = alarmTemplateRepository.findByAlarmType(alarmType);

        // then
        assertThat(findAlarmType).isNotEmpty();
        assertThat(findAlarmType.get())
            .extracting("alarmType", "title", "content")
            .containsExactly(alarmType, title, content);
    }
}