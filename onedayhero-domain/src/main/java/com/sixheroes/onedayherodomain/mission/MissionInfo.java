package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.util.Objects.requireNonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MissionInfo {

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "mission_date", nullable = false)
    private LocalDate missionDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "deadline_time", nullable = false)
    private LocalTime deadlineTime;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Builder
    private MissionInfo(
            String content,
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime,
            Integer price
    ) {
        requireNonNull(content);
        requireNonNull(missionDate);
        requireNonNull(startTime);
        requireNonNull(endTime);
        requireNonNull(deadlineTime);
        requireNonNull(price);
        validMissionInfoConstructType(content, missionDate, startTime, endTime, deadlineTime, price);
        this.content = content;
        this.missionDate = missionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadlineTime = deadlineTime;
        this.price = price;
    }

    private void validMissionInfoConstructType(
            String content,
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime,
            Integer price
    ) {
        validContentIsBlank(content);
        validContentInRange(content);
        validMissionDateTimeInRange(missionDate, startTime, endTime, deadlineTime);
        validPriceInPositive(price);
    }

    private void validContentIsBlank(String content) {
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }
    }

    private void validContentInRange(String content) {
        if (content.length() > 1000) {
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }
    }

    private void validMissionDateTimeInRange(LocalDate missionDate, LocalTime startTime, LocalTime endTime, LocalTime deadlineTime) {
        LocalDateTime startDateTime = LocalDateTime.of(missionDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(missionDate, endTime);
        LocalDateTime deadLineDateTime = LocalDateTime.of(missionDate, deadlineTime);

        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }

        if (deadLineDateTime.isAfter(startDateTime)) {
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }
    }

    private void validPriceInPositive(Integer price) {
        if (price < 0) {
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }
    }
}
