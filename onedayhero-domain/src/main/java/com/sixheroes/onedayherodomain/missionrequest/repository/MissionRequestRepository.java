package com.sixheroes.onedayherodomain.missionrequest.repository;

import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRequestRepository extends JpaRepository<MissionRequest, Long> {
}
