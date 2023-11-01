package com.sixheroes.onedayheroapplication.region;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegionReader {

    private final RegionRepository regionRepository;

    public Region findOne(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> {
                    log.debug("regionId에 해당하는 지역을 찾지 못하였습니다. regionId : {}", regionId);
                    return new NoSuchElementException(ErrorCode.T_001.name());
                });
    }
}
