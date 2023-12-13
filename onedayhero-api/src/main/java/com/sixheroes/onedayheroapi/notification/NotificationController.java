package com.sixheroes.onedayheroapi.notification;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.notification.NotificationService;
import com.sixheroes.onedayheroapplication.notification.response.AlarmResponse;
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
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Slice<AlarmResponse>>> findAlarm(
        @AuthUser Long userId,
        @PageableDefault Pageable pageable
    ) {
        var alarm = notificationService.findAlarm(userId, pageable);

        return ResponseEntity.ok(ApiResponse.ok(alarm));
    }

    @DeleteMapping("/{alarmId}")
    public ResponseEntity<ApiResponse<Void>> deleteAlarm(
        @AuthUser Long userId,
        @PathVariable String alarmId
    ) {
        notificationService.deleteAlarm(userId, alarmId);

        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
