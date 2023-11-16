package com.sixheroes.onedayherodomain.review;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_images")
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

    public void setReview(Review review) {
        this.review = review;
    }
}
