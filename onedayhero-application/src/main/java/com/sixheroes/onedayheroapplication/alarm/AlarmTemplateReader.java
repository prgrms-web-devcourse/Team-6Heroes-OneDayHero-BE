package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayheromongo.alarm.AlarmTemplate;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlarmTemplateReader {

    private static final long ALARM_TEMPLATE_RATE = 6 * 3600 * 1000L; // 6시간
    private final AlarmTemplateRepository alarmTemplateRepository;

    @Cacheable(value = "alarmTemplates")
    public AlarmTemplate findOne(
            String alarmType
    ) {
        return alarmTemplateRepository.findByAlarmType(alarmType)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ALARM_TEMPLATE));
    }

    @CacheEvict(value = "alarmTemplates", allEntries = true)
    @Scheduled(fixedRate = ALARM_TEMPLATE_RATE)
    public void emptyAlarmTemplateCache() {
        log.info("알림 템플릿 캐싱이 비워졌습니다.");
    }
}