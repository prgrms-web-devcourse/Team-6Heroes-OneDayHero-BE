package com.sixheroes.onedayheromongo.alarm;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayheromongo.global.BaseDocument;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.Objects;

@Getter
@Document(collection = "alarms")
public class Alarm extends BaseDocument {

    @MongoId
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @DBRef
    @Field(name = "alarm_template")
    private AlarmTemplate alarmTemplate;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "title")
    private String title;

    @Field(name = "content")
    private String content;

    @Builder
    private Alarm(
            AlarmTemplate alarmTemplate,
            Long userId,
            String title,
            String content
    ) {
        this.alarmTemplate = alarmTemplate;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public void validOwner(
            Long userId
    ) {
        if (!Objects.equals(this.userId, userId)) {
            throw new BusinessException(ErrorCode.INVALID_ALARM_OWNER);
        }
    }
}