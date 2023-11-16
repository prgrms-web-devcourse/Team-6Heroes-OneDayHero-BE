package com.sixheroes.onedayheromongodb.alarm.mongo;

import com.sixheroes.onedayheromongodb.alarm.AlarmTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface AlarmTemplateRepository extends MongoRepository<AlarmTemplate, String> {

    @Query("{'alarm_type' :  ?0}")
    Optional<AlarmTemplate> findByAlarmType(
        String alarmType
    );
}
