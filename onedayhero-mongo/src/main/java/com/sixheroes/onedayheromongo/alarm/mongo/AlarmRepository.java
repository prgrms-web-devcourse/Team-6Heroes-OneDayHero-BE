package com.sixheroes.onedayheromongo.alarm.mongo;

import com.sixheroes.onedayheromongo.alarm.Alarm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AlarmRepository extends MongoRepository<Alarm, String> {

    @Query(value = "{'user_id' : ?0}", sort = "{'created_at' :  -1}")
    Slice<Alarm> findAllByUserId(Long userId, Pageable pageable);
}
