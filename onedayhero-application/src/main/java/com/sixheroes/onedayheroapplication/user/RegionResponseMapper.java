package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.user.response.RegionForUserResponse;
import com.sixheroes.onedayherodomain.region.Region;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegionResponseMapper {

    public static Map<String, Map<String, List<RegionForUserResponse>>> toFavoriteRegions(
        List<Region> regions
    ) {
        return regions.stream()
            .collect(
                Collectors.groupingBy(
                    Region::getSi,
                    Collectors.groupingBy(
                        Region::getGu,
                        Collectors.mapping(RegionForUserResponse::from, Collectors.toList())
                    ))
            );
    }
}
