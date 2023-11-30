package com.sixheroes.onedayheroapplication.alarm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@ActiveProfiles("test")
@SpringBootTest
class AlarmTemplateReaderTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private AlarmTemplateReader alarmTemplateReader;

    @DisplayName("MongoDB에서 한 번 불러온 알림 템플릿은 다음부터는 캐싱에서 불러온다.")
    @Test
    void alarmTemplateCache() throws InterruptedException {
        // given
        var cacheName = "alarmTemplates";
        var alarmType = "MISSION_COMPLETED";

        // when
        alarmTemplateReader.findOne(alarmType);
        var cache = cacheManager.getCache(cacheName);

        // then
        assertThat(cache).isNotNull();
        assertThat(cache.get(alarmType)).isNotNull();
    }
}