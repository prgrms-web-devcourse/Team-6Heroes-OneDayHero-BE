package com.sixheroes.onedayherodomain.review.repository;

import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.review.repository.dto.ReviewCreateEventDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        select 
            new com.sixheroes.onedayherodomain.review.repository.dto.ReviewCreateEventDto(
                r.receiverId, u.userBasicInfo.nickname, r.id, r.missionTitle
            )
        from Review r
        join User u on u.id = r.senderId
        where r.id = :reviewId
    """)
    Optional<ReviewCreateEventDto> findReviewCreateEventById(
        @Param("reviewId") Long reviewId
    );
}
