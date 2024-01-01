package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.global.util.SliceResultConverter;
import com.sixheroes.onedayheroapplication.mission.MissionCategoryReader;
import com.sixheroes.onedayheroapplication.region.RegionReader;
import com.sixheroes.onedayheroapplication.user.reader.UserReader;
import com.sixheroes.onedayheroapplication.user.repository.dto.HeroRankQueryResponse;
import com.sixheroes.onedayheroapplication.user.request.HeroRankServiceRequest;
import com.sixheroes.onedayheroapplication.user.response.HeroRankResponse;
import com.sixheroes.onedayheroapplication.user.response.HeroSearchResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileCitizenResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileHeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

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

    public Slice<HeroRankResponse> findHeroesRank(
        HeroRankServiceRequest request,
        Pageable pageable
    ) {
        var categoryId = request.missionCategoryCode()
            .getCategoryId();
        var region = regionReader.findByDong(request.regionName());

        var heroesRank = userReader.findHeroesRank(region.getId(), categoryId, pageable);

        var heroRankResponses = addRank(heroesRank, pageable);
        return SliceResultConverter.consume(heroRankResponses, pageable);
    }

    private List<HeroRankResponse> addRank(
        List<HeroRankQueryResponse> heroRankQueryResponses,
        Pageable pageable
    ) {
        return IntStream.range(0, heroRankQueryResponses.size()).mapToObj(i -> {
            var heroRankQueryResponse = heroRankQueryResponses.get(i);
            return HeroRankResponse.of(heroRankQueryResponse, pageable, i);
        }).toList();
    }
}
