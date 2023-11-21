package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.region.Region;

public record RegionForUserResponse(
    Long id,
    String dong
) {

    public static RegionForUserResponse from(
        Region region
    ) {
        return new RegionForUserResponse(region.getId(), region.getDong());
    }
}
