package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayheromongo.alarm.Alarm;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

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
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.T_001.name()));
    }
}
