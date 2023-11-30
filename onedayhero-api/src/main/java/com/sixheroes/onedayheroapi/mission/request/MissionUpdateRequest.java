package com.sixheroes.onedayheroapi.mission.request;

import com.sixheroes.onedayheroapi.global.s3.MultipartFileMapper;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record MissionUpdateRequest(
        @NotNull(message = "미션의 카테고리 아이디는 필수 값 입니다.")
        Long missionCategoryId,

        @NotNull(message = "동 이름은 필수 값 입니다.")
        String regionName,

        @NotNull(message = "위도는 필수 값 입니다.")
        Double latitude,

        @NotNull(message = "경도는 필수 값 입니다.")
        Double longitude,

        @Valid
        MissionInfoRequest missionInfo
) {

    public MissionUpdateServiceRequest toService(
            List<MultipartFile> multipartFiles,
            Long userId
    ) {
        return MissionUpdateServiceRequest.builder()
                .missionCategoryId(missionCategoryId)
                .regionName(regionName)
                .userId(userId)
                .latitude(latitude)
                .longitude(longitude)
                .missionInfo(missionInfo.toService())
                .imageFiles(MultipartFileMapper.mapToServiceRequests(multipartFiles))
                .build();
    }
}
