package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MissionInfo {

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "mission_date", nullable = false)
    private LocalDate missionDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "deadline_time", nullable = false)
    private LocalDateTime deadlineTime;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Builder
    private MissionInfo(
            String title,
            String content,
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime,
            Integer price,
            LocalDateTime serverTime
    ) {
        validMissionTitle(title);
        validMissionContent(content);
        validPriceIsPositive(price);
        validMissionDateTimeInRange(missionDate, startTime, endTime, deadlineTime, serverTime);

        this.title = title;
        this.content = content;
        this.missionDate = missionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadlineTime = deadlineTime;
        this.price = price;
    }

    public MissionInfo extend(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime,
            LocalDateTime serverTime
    ) {
        validMissionDateTimeInRange(missionDate, startTime, endTime, deadlineTime, serverTime);
        this.missionDate = missionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadlineTime = deadlineTime;

        return this;
    }

    private void validMissionTitle(String title) {
        validTitleIsEmpty(title);
        validTitleInRange(title);
    }

    private void validTitleIsEmpty(String title) {
        if (!StringUtils.hasText(title)) {
            log.warn("미션의 제목은 null 이거나 공백 일 수 없습니다. title : {}", title);
            throw new BusinessException(ErrorCode.INVALID_MISSION_TITLE);
        }
    }

    private void validTitleInRange(String title) {
        if (title.length() > 100) {
            log.warn("미션의 제목의 길이는 100자 이하여야합니다. title 길이 : {}", title.length());
            throw new BusinessException(ErrorCode.INVALID_MISSION_TITLE_LENGTH);
        }
    }

    private void validMissionDateTimeInRange(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime,
            LocalDateTime serverTime
    ) {
        var startDateTime = LocalDateTime.of(missionDate, startTime);
        var endDateTime = LocalDateTime.of(missionDate, endTime);

        if (missionDate.isBefore(serverTime.toLocalDate())) {
            log.warn("미션의 수행 날짜가 현재 날짜보다 이전 일 수 없습니다. 수행 일 : {}, 현재 날짜 : {}", missionDate, serverTime.toLocalDate());
            throw new BusinessException(ErrorCode.INVALID_MISSION_DATE);
        }

        if (endDateTime.isBefore(startDateTime)) {
            log.warn("미션의 종료 시간이 시작 시간보다 이전 일 수 없습니다. 시작 시간 : {}, 종료 시간 : {}", startDateTime, endDateTime);
            throw new BusinessException(ErrorCode.INVALID_MISSION_END_TIME);
        }

        if (deadlineTime.isAfter(startDateTime)) {
            log.warn("미션의 마감 시간이 시작 시간 이후 일 수 없습니다. 시작 시간 : {}, 마감 시간 : {}", startDateTime, deadlineTime);
            throw new BusinessException(ErrorCode.INVALID_MISSION_DEADLINE_TIME);
        }
    }

    private void validMissionContent(
            String content
    ) {
        validContentIsBlank(content);
        validContentInRange(content);
    }

    private void validContentIsBlank(String content) {
        if (!StringUtils.hasText(content)) {
            log.warn("미션의 콘텐츠는 null 이거나 공백 일 수 없습니다. content : {}", content);
            throw new BusinessException(ErrorCode.INVALID_MISSION_CONTENT);
        }
    }

    private void validContentInRange(String content) {
        if (content.length() > 1000) {
            log.warn("미션의 콘텐츠의 길이는 1000자 이하여야합니다. contentSize : {}", content.length());
            throw new BusinessException(ErrorCode.INVALID_MISSION_CONTENT_LENGTH);
        }
    }

    private void validPriceIsPositive(Integer price) {
        if (price < 0) {
            log.warn("미션의 포상금은 0원 이상이어야 합니다. price : {}", price);
            throw new BusinessException(ErrorCode.INVALID_MISSION_PRICE);
        }
    }
}
