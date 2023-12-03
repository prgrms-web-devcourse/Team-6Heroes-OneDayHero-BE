package com.sixheroes.onedayherodomain.review;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE reviews SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(
        name = "reviews",
        indexes = {
                @Index(name = "reviews_receiver_id_idx", columnList = "receiver_id")
        }
)
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "mission_title", length = 100, nullable = false)
    private String missionTitle;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "star_score", columnDefinition = "SMALLINT", nullable = false)
    private Integer starScore;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Builder
    private Review(
            Long categoryId,
            String missionTitle,
            Long senderId,
            Long receiverId,
            Integer starScore,
            String content
    ) {
        this.categoryId = categoryId;
        this.missionTitle = missionTitle;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.starScore = starScore;
        this.content = content;
        this.isDeleted = false;
    }

    public void update(
            String content,
            Integer starScore
    ) {
        this.content = content;
        this.starScore = starScore;
    }

    public void addImage(
            ReviewImage reviewImage
    ) {
            this.reviewImages.add(reviewImage);
            reviewImage.setReview(this);
    }

    public boolean hasImage() {
        return !this.getReviewImages().isEmpty();
    }
}
