package com.sixheroes.onedayheroapplication.review.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewQueryResponse;
import com.sixheroes.onedayheroapplication.review.response.SentReviewQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;



@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<SentReviewQueryResponse> viewSentReviews(
            Pageable pageable,
            Long userId
    ) {
        throw new UnsupportedOperationException();
    }

    public Slice<ReceivedReviewQueryResponse> viewReceivedReviews(
            Pageable pageable,
            Long userId
    ) {
        throw new UnsupportedOperationException();
    }
}
