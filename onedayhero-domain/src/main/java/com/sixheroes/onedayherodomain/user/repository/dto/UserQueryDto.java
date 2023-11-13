package com.sixheroes.onedayherodomain.user.repository.dto;

import com.sixheroes.onedayherodomain.user.UserGender;
import com.sixheroes.onedayherodomain.user.Week;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UserQueryDto(
    Long userId,
    String nickname,
    UserGender gender,
    LocalDate birth,
    String introduce,
    List<Week> favoriteDate,
    LocalTime favoriteStartTime,
    LocalTime favoriteEndTime,
    Long imageId,
    String originalName,
    String uniqueName,
    String path,
    Integer heroScore,
    Boolean isHeroMode
) {
}
