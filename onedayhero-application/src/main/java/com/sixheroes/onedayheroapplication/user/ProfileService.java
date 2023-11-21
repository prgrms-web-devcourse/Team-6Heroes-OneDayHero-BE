package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.region.RegionReader;
import com.sixheroes.onedayheroapplication.user.response.ProfileCitizenResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileHeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserReader userReader;
    private final RegionReader regionReader;

    public ProfileCitizenResponse findCitizenProfile(
        Long userId
    ) {
        var user = userReader.findOne(userId);

        return ProfileCitizenResponse.from(user);
    }

    public ProfileHeroResponse findHeroProfile(
        Long userId
    ) {
        var user = userReader.findOne(userId);
        user.validPossibleHeroProfile();

        var regions = regionReader.findByUser(user);

        return ProfileHeroResponse.from(user, regions);
    }
}
