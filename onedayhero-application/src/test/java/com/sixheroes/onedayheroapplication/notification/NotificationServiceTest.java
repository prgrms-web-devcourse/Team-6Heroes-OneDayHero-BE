package com.sixheroes.onedayheroapplication.notification;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.notification.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.notification.dto.SsePayload;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalAction;
import com.sixheroes.onedayherocommon.converter.StringConverter;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayheromongo.alarm.Alarm;
import com.sixheroes.onedayheromongo.alarm.AlarmTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotificationServiceTest extends IntegrationApplicationEventTest {

    @DisplayName("알림 형식에 맞춰 알림을 생성한 뒤 SSE 이벤트를 발행한다.")
    @Test
    void notifyClientPublishEvent() {
        // given
        var alarmTemplate = createAlarmTemplate();
        alarmTemplateRepository.save(alarmTemplate);

        var data = new HashMap<String, Object>() {
            {
                put("citizenNickname", "선량한 시민");
                put("missionId", 1L);
                put("missionTitle", "[급함] 편의점 알바 대타 구합니다.");
            }
        };

        var userId = 1L;
        var alarmPayload = AlarmPayload.builder()
                .alarmType(alarmTemplate.getAlarmType())
                .userId(userId)
                .data(data)
                .build();

        var title = StringConverter.convertMapToString(data, alarmTemplate.getTitle());
        var content = StringConverter.convertMapToString(data, alarmTemplate.getContent());
        var alarm = Alarm.builder()
                .alarmTemplate(alarmTemplate)
                .userId(userId)
                .title(title)
                .content(content)
                .build();

        given(alarmTemplateRepository.findByAlarmType(anyString())).willReturn(Optional.of(alarmTemplate));
        given(alarmRepository.save(any(Alarm.class))).willReturn(alarm);

        // when
        notificationService.notifyClient(alarmPayload);

        // then
        verify(alarmTemplateRepository, times(1)).findByAlarmType(anyString());
        verify(alarmRepository, times(1)).save(any(Alarm.class));

        var ssePaylodOptional = applicationEvents.stream(SsePayload.class).findFirst();
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

        given(alarmTemplateRepository.findByAlarmType(anyString())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> notificationService.notifyClient(alarmPayload))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private AlarmTemplate createAlarmTemplate() {
        return AlarmTemplate.builder()
                .alarmType(MissionProposalAction.MISSION_PROPOSAL_CREATE.name())
                .title("미션을 제안받았습니다.")
                .content("${citizenNickname}님으로부터 '${missionTitle}' 미션을 제안받았습니다.")
                .build();
    }
}