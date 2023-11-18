package com.sixheroes.onedayheroapplication.region;

import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class RegionCacheTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionService regionService;

    @DisplayName("이미 불러온 전체 지역 데이터는 한 번 만 불러온다.")
    @Test
    void regionCacheRead() {
        // given
        var regionA = Region.builder()
                .si("서울특별시")
                .gu("강남구")
                .dong("삼성1동")
                .build();

        var regionB = Region.builder()
                .si("서울특별시")
                .gu("서초구")
                .dong("양재1동")
                .build();

        regionRepository.saveAll(List.of(regionA, regionB));

        // when
        var allRegions = regionService.findAllRegions();

        var regionCache = cacheManager.getCache("regions");

        // then
        assertThat(regionCache).isNotNull();
    }
}
