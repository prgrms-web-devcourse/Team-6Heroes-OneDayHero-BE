package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.mission.MissionCategoryReader;
import com.sixheroes.onedayheroapplication.region.RegionReader;
import com.sixheroes.onedayheroapplication.user.response.HeroSearchResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileCitizenResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileHeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserReader userReader;
    private final RegionReader regionReader;
    private final MissionCategoryReader missionCategoryReader;

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

    public Slice<HeroSearchResponse> searchHeroes(
        String nickname,
        Pageable pageable
    ) {
        var heroes = userReader.findHeroes(nickname, pageable);

        var missionCategories = missionCategoryReader.findAllByUser(heroes.getContent());

        return heroes.map(u -> HeroSearchResponse.of(u, missionCategories.get(u.getId())));
    }
}
