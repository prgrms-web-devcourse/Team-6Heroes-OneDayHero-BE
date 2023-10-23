package com.sixheroes.onedayherodomain.missionreview;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "star_score", nullable = false)
    private Integer starScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "reviewer_type", nullable = false)
    private ReviewerType reviewerType;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;
}
