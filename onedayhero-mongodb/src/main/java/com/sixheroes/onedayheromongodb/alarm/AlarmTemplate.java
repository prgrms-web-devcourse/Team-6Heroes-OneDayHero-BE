package com.sixheroes.onedayheromongodb.alarm;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Document(collection = "alarm_templates")
public class AlarmTemplate {

    @MongoId
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "alarm_type")
    private String alarmType;

    @Field(name = "title")
    private String title;

    @Field(name = "content")
    private String content;

    @Builder
    private AlarmTemplate(
        String alarmType,
        String title,
        String content
    ) {
        this.alarmType = alarmType;
        this.title = title;
        this.content = content;
    }
}