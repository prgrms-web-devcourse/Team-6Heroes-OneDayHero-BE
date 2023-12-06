package com.sixheroes.onedayherodomain.review;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "review_images",
        indexes = {
                @Index(name = "review_images_id_idx", columnList = "review_id")
        }
)
@Entity
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "review_id", nullable = false)
    private Review review;

    @Column(name = "original_name", length = 260, nullable = false)
    private String originalName;

    @Column(name = "unique_name", length = 100, nullable = false)
    private String uniqueName;

    @Column(name = "path", length = 250, nullable = false)
    private String path;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewImage(
            String originalName,
            String uniqueName,
            String path
    ) {
        this.originalName = originalName;
        this.uniqueName = uniqueName;
        this.path = path;
    }

    public static ReviewImage createReviewImage(
            String originalName,
            String uniqueName,
            String path
    ) {
        return ReviewImage.builder()
                .originalName(originalName)
                .uniqueName(uniqueName)
                .path(path)
                .build();
    }

    public void setReview(
            Review review
    ) {
        this.review = review;
    }

    public void validOwn(
            Long userId
    ) {
        if (!Objects.equals(review.getSenderId(), userId)) {
            log.warn("리뷰 소유자가 아닙니다. userId : {}, senderId : {}", userId, review.getSenderId());
            throw new BusinessException(ErrorCode.INVALID_REVIEW_OWNER);
        }
    }
}
