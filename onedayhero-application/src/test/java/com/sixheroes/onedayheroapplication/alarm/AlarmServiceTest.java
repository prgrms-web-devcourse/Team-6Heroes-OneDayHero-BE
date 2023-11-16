package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.alarm.dto.SsePaylod;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalAction;
import com.sixheroes.onedayherocommon.converter.StringConverter;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayheromongodb.alarm.AlarmTemplate;
import com.sixheroes.onedayheromongodb.alarm.mongo.AlarmRepository;
import com.sixheroes.onedayheromongodb.alarm.mongo.AlarmTemplateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RecordApplicationEvents
class AlarmServiceTest extends IntegrationApplicationTest {

    @Autowired
    private AlarmTemplateRepository alarmTemplateRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private ApplicationEvents applicationEvents;

    @AfterEach
    void teardown() {
        alarmTemplateRepository.deleteAll();
        alarmRepository.deleteAll();
    }

    @DisplayName("알림 형식에 맞춰 알림을 생성한다.")
    @Test
    void notifyClient() {
        // given
        var alarmTemplate = createAlarmTemplate();
        alarmTemplateRepository.save(alarmTemplate);

        var data = new HashMap<String, Object>() { {
            put("citizenNickname", "선량한 시민");
            put("missionId", 1L);
            put("missionTitle", "[급함] 편의점 알바 대타 구합니다.");
        }};

        var userId = 1L;
        var alarmPayload = AlarmPayload.builder()
            .alarmType(alarmTemplate.getAlarmType())
            .userId(userId)
            .data(data)
            .build();

        var title = StringConverter.convertMapToString(data, alarmTemplate.getTitle());
        var content = StringConverter.convertMapToString(data, alarmTemplate.getContent());

        // when
        var alarmId = alarmService.notifyClient(alarmPayload);

        // then
        var alarmOptional = alarmRepository.findById(alarmId);
        assertThat(alarmOptional).isNotEmpty();
        var alarm = alarmOptional.get();
        assertThat(alarm).extracting("userId", "title", "content")
            .containsExactly(userId, title, content);
    }

    @DisplayName("알림 형식에 맞춰 알림을 생성한 뒤 SSE 이벤트를 발행한다.")
    @Test
    void notifyClientPublishEvent() {
        // given
        var alarmTemplate = createAlarmTemplate();
        alarmTemplateRepository.save(alarmTemplate);

        var data = new HashMap<String, Object>() { {
            put("citizenNickname", "선량한 시민");
            put("missionId", 1L);
            put("missionTitle", "[급함] 편의점 알바 대타 구합니다.");
        }};

        var userId = 1L;
        var alarmPayload = AlarmPayload.builder()
            .alarmType(alarmTemplate.getAlarmType())
            .userId(userId)
            .data(data)
            .build();

        var title = StringConverter.convertMapToString(data, alarmTemplate.getTitle());
        var content = StringConverter.convertMapToString(data, alarmTemplate.getContent());

        // when
        alarmService.notifyClient(alarmPayload);

        // then
        var ssePaylodOptional = applicationEvents.stream(SsePaylod.class).findFirst();
        assertThat(ssePaylodOptional).isNotEmpty();
        var ssePaylod = ssePaylodOptional.get();
        assertThat(ssePaylod.alarmType()).isEqualTo(MissionProposalAction.MISSION_PROPOSAL_CREATE.name());
        assertThat(ssePaylod.userId()).isEqualTo(userId);
        assertThat(ssePaylod.data())
            .extracting("title", "content")
            .containsExactly(title, content);
    }

    @DisplayName("알림 형식에 맞춰 알림을 생성할 때 존재하지 않는 알림 타입이면 예외가 발생한다.")
    @Test
    void notifyClientWhenNotExist() {
        // given
        var notExistAlarmType = "weired alarm type";
        var userId = 1L;
        var alarmPayload = AlarmPayload.builder()
            .alarmType(notExistAlarmType)
            .userId(userId)
            .build();

        // when
        assertThatThrownBy(() -> alarmService.notifyClient(alarmPayload))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EAT_000.name());
    }

    private AlarmTemplate createAlarmTemplate() {
        return AlarmTemplate.builder()
            .alarmType(MissionProposalAction.MISSION_PROPOSAL_CREATE.name())
            .title("미션을 제안받았습니다.")
            .content("${citizenNickname}님으로부터 '${missionTitle}' 미션을 제안받았습니다.")
            .build();
    }
}