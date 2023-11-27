package com.sixheroes.onedayherodomain.user;

import jakarta.persistence.AttributeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FavoriteDateConverter implements AttributeConverter<List<Week>, String> {

    @Override
    public String convertToDatabaseColumn(List<Week> attribute) {
        if (Objects.isNull(attribute) || attribute.isEmpty()) {
            return null;
        }

        return attribute.stream()
                    .map(Week::name)
                    .collect(Collectors.joining(","));
    }

    @Override
    public List<Week> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(dbData.split(","))
                    .map(Week::valueOf)
                    .toList();
    }
}
