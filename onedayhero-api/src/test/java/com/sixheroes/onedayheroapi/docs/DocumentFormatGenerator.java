package com.sixheroes.onedayheroapi.docs;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public interface DocumentFormatGenerator {

    static Attributes.Attribute getDateFormat() { // 날짜에 대한 data 포맷
        return key("format").value("yyyy-MM-dd");
    }

    static Attributes.Attribute getTimeFormat() {
        return key("format").value("HH:mm");
    }

    static Attributes.Attribute getDateTimeFormat() {
        return key("format").value("yyyy-MM-dd'T'HH:mm:ss");
    }
}
