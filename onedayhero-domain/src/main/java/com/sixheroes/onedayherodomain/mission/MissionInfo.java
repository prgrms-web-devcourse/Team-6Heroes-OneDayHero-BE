package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
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
    private LocalTime deadlineTime;

    @Column(name = "price", nullable = false)
    private Integer price;

    //TODO : Refactoring
    //많은 검증 로직을 Embeddable로 바꿔서 검증 로직 분리
    @Builder
    private MissionInfo(
            String title,
            String content,
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime,
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

    private void validMissionTitle(String title) {
        validTitleIsEmpty(title);
        validTitleInRange(title);
    }

    private void validTitleIsEmpty(String title) {
        if (!StringUtils.hasText(title)) {
            log.warn("미션의 제목은 null 이거나 공백 일 수 없습니다. title : {}", title);
            throw new IllegalArgumentException(ErrorCode.EM_001.name());
        }
    }

    private void validTitleInRange(String title) {
        if (title.length() > 100) {
            log.warn("미션의 제목의 길이는 100자 이하여야합니다. title 길이 : {}", title.length());
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }
    }

    private void validMissionDateTimeInRange(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime,
            LocalDateTime serverTime
    ) {
        var startDateTime = LocalDateTime.of(missionDate, startTime);
        var endDateTime = LocalDateTime.of(missionDate, endTime);
        var deadLineDateTime = LocalDateTime.of(missionDate, deadlineTime);

        if (missionDate.isBefore(serverTime.toLocalDate())) {
            log.warn("미션의 수행 날짜가 현재 날짜보다 이전 일 수 없습니다. 수행 일 : {}, 현재 날짜 : {}", missionDate, serverTime.toLocalDate());
            throw new IllegalArgumentException(ErrorCode.EM_003.name());
        }

        if (endDateTime.isBefore(startDateTime)) {
            log.warn("미션의 종료 시간이 시작 시간보다 이전 일 수 없습니다. 시작 시간 : {}, 종료 시간 : {}", startDateTime, endDateTime);
            throw new IllegalArgumentException(ErrorCode.EM_004.name());
        }

        if (deadLineDateTime.isAfter(startDateTime)) {
            log.warn("미션의 마감 시간이 시작 시간 이후 일 수 없습니다. 시작 시간 : {}, 마감 시간 : {}", startDateTime, deadLineDateTime);
            throw new IllegalArgumentException(ErrorCode.EM_005.name());
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
            throw new IllegalArgumentException(ErrorCode.EM_001.name());
        }
    }

    private void validContentInRange(String content) {
        if (content.length() > 1000) {
            log.warn("미션의 콘텐츠의 길이는 1000자 이하여야합니다. contentSize : {}", content.length());
            throw new IllegalArgumentException(ErrorCode.EM_002.name());
        }
    }

    private void validPriceIsPositive(Integer price) {
        if (price < 0) {
            log.warn("미션의 포상금은 0원 이상이어야 합니다. price : {}", price);
            throw new IllegalArgumentException(ErrorCode.EM_006.name());
        }
    }
}
