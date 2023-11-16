package com.sixheroes.onedayheromongodb.alarm.mongo;

import com.sixheroes.onedayheromongodb.alarm.Alarm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlarmRepository extends MongoRepository<Alarm, String> {
}
