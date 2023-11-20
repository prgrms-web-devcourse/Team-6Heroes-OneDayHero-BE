package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayheromongo.alarm.AlarmTemplate;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class AlarmTemplateReader {

    private final AlarmTemplateRepository alarmTemplateRepository;

    public AlarmTemplate findOne(
        String alarmType
    ) {
        return alarmTemplateRepository.findByAlarmType(alarmType)
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.EAT_000.name()));
    }
}
