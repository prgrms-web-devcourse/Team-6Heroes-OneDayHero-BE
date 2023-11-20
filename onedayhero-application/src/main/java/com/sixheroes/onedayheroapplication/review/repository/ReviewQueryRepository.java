package com.sixheroes.onedayheroapplication.review.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroapplication.global.util.SliceResultConverter;
import com.sixheroes.onedayheroapplication.review.repository.response.ReviewDetailQueryResponse;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewQueryResponse;
import com.sixheroes.onedayheroapplication.review.response.ReviewImageResponse;
import com.sixheroes.onedayheroapplication.review.response.SentReviewQueryResponse;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.QMissionCategory;
import com.sixheroes.onedayherodomain.review.QReview;
import com.sixheroes.onedayherodomain.review.QReviewImage;
import com.sixheroes.onedayherodomain.user.QUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sixheroes.onedayherodomain.mission.QMissionCategory.missionCategory;
import static com.sixheroes.onedayherodomain.review.QReview.review;
import static com.sixheroes.onedayherodomain.review.QReviewImage.reviewImage;
import static com.sixheroes.onedayherodomain.user.QUser.user;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<SentReviewQueryResponse> viewSentReviews(
            Pageable pageable,
            Long userId
    ) {
        var content = queryFactory
                .select(
                        Projections.constructor(
                                SentReviewQueryResponse.class,
                                review.id,
                                missionCategory.name,
                                review.missionTitle,
                                review.starScore,
                                review.createdAt
                        )
                )
                .from(review)
                .join(missionCategory).on(review.categoryId.eq(missionCategory.id))
                .where(review.senderId.eq(userId))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return SliceResultConverter.consume(
                content,
                pageable
        );
    }

    public Slice<ReceivedReviewQueryResponse> viewReceivedReviews(
            Pageable pageable,
            Long userId
    ) {
        var content = queryFactory
                .select(
                        Projections.constructor(
                                ReceivedReviewQueryResponse.class,
                                review.id,
                                review.senderId,
                                missionCategory.name,
                                review.missionTitle,
                                review.starScore,
                                review.createdAt
                        )
                )
                .from(review)
                .join(missionCategory).on(review.categoryId.eq(missionCategory.id));


        throw new UnsupportedOperationException();
    }

    public Optional<ReviewDetailQueryResponse> viewReviewDetail(
            Long reviewId
    ) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                Projections.constructor(
                                        ReviewDetailQueryResponse.class,
                                        review.id,
                                        review.senderId,
                                        user.userBasicInfo.nickname,
                                        review.receiverId,
                                        missionCategory.id,
                                        missionCategory.missionCategoryCode,
                                        missionCategory.name,
                                        review.missionTitle,
                                        review.content,
                                        review.starScore,
                                        review.createdAt
                                )
                        )
                        .from(review)
                        .join(user).on(review.senderId.eq(user.id))
                        .join(missionCategory).on(review.categoryId.eq(missionCategory.id))
                        .where(review.id.eq(reviewId))
                        .fetchOne()
        );
    }
}
