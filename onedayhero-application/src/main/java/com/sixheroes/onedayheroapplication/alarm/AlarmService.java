package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.alarm.dto.SsePaylod;
import com.sixheroes.onedayheromongodb.alarm.mongo.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;

    private final AlarmTemplateReader alarmTemplateReader;

    private final ApplicationEventPublisher applicationEventPublisher;

            public String notifyClient(
        AlarmPayload alarmPayload
    ) {
        var alarmType = alarmPayload.alarmType();
        var alarmTemplate = alarmTemplateReader.findOne(alarmType);

        var alarm = alarmPayload.toEntity(alarmTemplate);
        alarmRepository.save(alarm);

        applicationEventPublisher.publishEvent(SsePaylod.of(alarmType, alarm));
        return alarm.getId();
    }
}
