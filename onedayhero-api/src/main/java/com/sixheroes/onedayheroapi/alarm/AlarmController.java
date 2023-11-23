package com.sixheroes.onedayheroapi.alarm;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.alarm.AlarmService;
import com.sixheroes.onedayheroapplication.alarm.response.AlarmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<ApiResponse<Slice<AlarmResponse>>> findAlarm(
        @AuthUser Long userId,
        @PageableDefault Pageable pageable
    ) {
        var alarm = alarmService.findAlarm(userId, pageable);

        return ResponseEntity.ok(ApiResponse.ok(alarm));
    }

    @DeleteMapping("/{alarmId}")
    public ResponseEntity<ApiResponse<Void>> deleteAlarm(
        @AuthUser Long userId,
        @PathVariable String alarmId
    ) {
        alarmService.deleteAlarm(userId, alarmId);

        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
