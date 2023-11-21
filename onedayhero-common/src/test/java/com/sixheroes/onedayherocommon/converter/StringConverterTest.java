package com.sixheroes.onedayherocommon.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class StringConverterTest {

    @DisplayName("형식에 맞춰 포맷팅한다.")
    @Test
    void test() {
        var format = "${nickname}님이 '${missionTitle}'를 제안했습니다.";
        Map<String, Object> map = Map.of("nickname", "선량한 시민", "missionTitle", "[급함!!] 알바 대타구합니다!!");

        var s = StringConverter.convertMapToString(map, format);
        Assertions.assertThat(s).isEqualTo("선량한 시민님이 '[급함!!] 알바 대타구합니다!!'를 제안했습니다.");
    }

}