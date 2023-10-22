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

    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "original_name", nullable = false)
    private String original_name;

    @Column(name = "unique_name", nullable = false)
    private String unique_name;

    @Column(name = "paths", nullable = false)
    private String path;
}
