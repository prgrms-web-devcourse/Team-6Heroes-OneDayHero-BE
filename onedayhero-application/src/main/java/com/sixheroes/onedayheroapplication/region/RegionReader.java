package com.sixheroes.onedayheroapplication.region;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayherodomain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class RegionReader {

    private final RegionRepository regionRepository;

    public Region findOne(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> {
                    log.warn("regionId에 해당하는 지역을 찾지 못하였습니다. regionId : {}", regionId);
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_REGION);
                });
    }

    public List<Region> findAll(
            List<Long> regionIds
    ) {
        return regionRepository.findAllById(regionIds);
    }

    public List<Region> findByUser(
            User user
    ) {
        return regionRepository.findByUser(user);
    }

    public Region findByDong(
            String dong
    ) {
        return regionRepository.findByDong(dong)
                .orElseThrow(() -> {
                    log.warn("일치하는 동 이름이 없습니다. dong : {}", dong);
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_REGION);
                });
    }
}
