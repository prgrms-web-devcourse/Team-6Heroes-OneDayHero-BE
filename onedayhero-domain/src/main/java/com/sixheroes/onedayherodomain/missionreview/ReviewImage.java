package com.sixheroes.onedayherodomain.missionreview;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_images")
@Entity
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "original_name", length = 260, nullable = false)
    private String originalName;

    @Column(name = "unique_name", length = 100, nullable = false)
    private String uniqueName;

    @Column(name = "path", length = 250, nullable = false)
    private String path;
}
