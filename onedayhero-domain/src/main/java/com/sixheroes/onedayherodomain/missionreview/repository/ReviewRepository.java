package com.sixheroes.onedayherodomain.missionreview.repository;

import com.sixheroes.onedayherodomain.missionreview.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
