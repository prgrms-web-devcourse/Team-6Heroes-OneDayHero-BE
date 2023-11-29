package com.sixheroes.onedayherodomain.mission.repository.response;

import com.sixheroes.onedayherodomain.mission.MissionStatus;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface MainMissionQueryResponse {
    Long getId();

    Long getCategoryId();

    String getCategoryCode();

    String getCategoryName();

    Long getCitizenId();

    Long getRegionId();

    String getSi();

    String getGu();

    String getDong();

    Point getLocation();

    String getTitle();

    LocalDate getMissionDate();

    LocalTime getStartTime();

    LocalTime getEndTime();

    LocalDateTime getDeadlineTime();

    Integer getPrice();

    Integer getBookmarkCount();

    MissionStatus getMissionStatus();

    Long getBookmarkId();
}
