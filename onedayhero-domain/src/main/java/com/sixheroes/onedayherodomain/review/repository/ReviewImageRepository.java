package com.sixheroes.onedayherodomain.review.repository;

import com.sixheroes.onedayherodomain.review.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query("select ri from ReviewImage ri where ri.review.id = :reviewId")
    Optional<List<ReviewImage>> findByReviewId(Long reviewId);
}
