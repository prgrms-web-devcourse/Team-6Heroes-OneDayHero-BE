package com.sixheroes.onedayheroapi.region;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.region.RegionService;
import com.sixheroes.onedayheroapplication.region.response.AllRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AllRegionResponse>>> findAllRegions() {
        var result = regionService.findAllRegions();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
