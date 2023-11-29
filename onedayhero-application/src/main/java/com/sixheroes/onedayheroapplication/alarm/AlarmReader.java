package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayheromongo.alarm.Alarm;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlarmReader {

    private final AlarmRepository alarmRepository;

    public Slice<Alarm> findAll(
            Long userId,
            Pageable pageable
    ) {
        return alarmRepository.findAllByUserId(userId, pageable);
    }

    public Alarm findOne(
            String alarmId
    ) {
        return alarmRepository.findById(alarmId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 알람입니다. alarmId : {}", alarmId);
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_ALARM);
                });
    }
}
