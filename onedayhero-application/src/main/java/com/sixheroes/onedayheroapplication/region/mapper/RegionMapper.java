package com.sixheroes.onedayheroapplication.region.mapper;

import com.sixheroes.onedayheroapplication.region.response.AllRegionResponse;
import com.sixheroes.onedayherodomain.region.Region;

import java.util.List;
import java.util.stream.Collectors;

public final class RegionMapper {

    public static List<AllRegionResponse> mapToAllRegionResponse(List<Region> regions) {
        var siResponse = regions.stream()
                .collect(Collectors.groupingBy(Region::getSi));

        return siResponse.entrySet()
                .stream()
                .map(entry -> new AllRegionResponse(
                        entry.getKey(),
                        mapToGuResponse(entry.getValue())
                )).toList();
    }

    private static List<AllRegionResponse.GuResponse> mapToGuResponse(List<Region> regions) {
        var guResponse = regions.stream()
                .collect(Collectors.groupingBy(Region::getGu));

        return guResponse.entrySet().stream()
                .map(entry -> new AllRegionResponse.GuResponse(
                        entry.getKey(),
                        mapToDongResponse(entry.getValue())
                )).toList();
    }

    private static List<AllRegionResponse.DongResponse> mapToDongResponse(List<Region> regions) {
        return regions.stream()
                .map(region -> new AllRegionResponse.DongResponse(
                        region.getId(),
                        region.getDong()
                )).toList();
    }
}
