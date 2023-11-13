package com.sixheroes.onedayherodomain.review.repository;

import com.sixheroes.onedayherodomain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
