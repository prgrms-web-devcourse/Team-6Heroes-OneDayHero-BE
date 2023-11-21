package com.sixheroes.onedayherocommon.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringConverter {

    private static final String ALARM_FORMAT = "${%s}";

    /***
     * @param
     */
    public static String convertMapToString(
        final Map<String, Object> data,
        String format
    ) {
        for (Map.Entry<String,Object> entry : data.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            format = format.replace(ALARM_FORMAT.formatted(key), Objects.toString(value));
        }
        return format;
    }
}
