package com.sixheroes.onedayherocommon.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeConverter {

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
    
    public static String convertDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String convertTimetoString(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
