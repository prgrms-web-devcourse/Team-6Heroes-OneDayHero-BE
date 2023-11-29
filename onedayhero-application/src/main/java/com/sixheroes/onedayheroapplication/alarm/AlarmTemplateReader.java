package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayheromongo.alarm.AlarmTemplate;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AlarmTemplateReader {

    private final AlarmTemplateRepository alarmTemplateRepository;

    public AlarmTemplate findOne(
            String alarmType
    ) {
        return alarmTemplateRepository.findByAlarmType(alarmType)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ALARM_TEMPLATE));
    }
}
