package com.sixheroes.onedayheroapplication.region;

import com.sixheroes.onedayheroapplication.region.mapper.RegionMapper;
import com.sixheroes.onedayheroapplication.region.response.AllRegionResponse;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RegionService {

    private final RegionRepository regionRepository;

    @Cacheable(value = "regions")
    public List<AllRegionResponse> findAllRegions() {
        var regions = regionRepository.findAll();

        return RegionMapper.mapToAllRegionResponse(regions);
    }
}
