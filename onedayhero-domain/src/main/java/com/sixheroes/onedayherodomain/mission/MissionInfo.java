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
        validMissionInfoConstructValue(content, missionDate, startTime, endTime, deadlineTime, price);
        this.content = content;
        this.missionDate = missionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadlineTime = deadlineTime;
        this.price = price;
    }

    private void validMissionInfoConstructValue(
            String content,
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime,
            Integer price
    ) {
        validContentIsBlank(content);
        validContentInRange(content);
        validMissionDateTimeInRange(missionDate, startTime, endTime, deadlineTime, LocalDateTime.now());
        validPriceIsPositive(price);
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

    public void validMissionDateTimeInRange(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime,
            LocalDateTime now
    ) {
        var startDateTime = LocalDateTime.of(missionDate, startTime);
        var endDateTime = LocalDateTime.of(missionDate, endTime);
        var deadLineDateTime = LocalDateTime.of(missionDate, deadlineTime);

        if (missionDate.isBefore(now.toLocalDate())) {
            log.warn("미션의 수행 날짜가 현재 날짜보다 이전 일 수 없습니다. 수행 일 : {}, 현재 날짜 : {}", missionDate, now.toLocalDate());
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

    private void validPriceIsPositive(Integer price) {
        if (price < 0) {
            log.warn("미션의 포상금은 0원 이상이어야 합니다. price : {}", price);
            throw new IllegalArgumentException(ErrorCode.EM_006.name());
        }
    }
}
