package com.sixheroes.onedayheromongo.alarm.mongo;

import com.sixheroes.onedayheromongo.alarm.Alarm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlarmRepository extends MongoRepository<Alarm, String> {
}
