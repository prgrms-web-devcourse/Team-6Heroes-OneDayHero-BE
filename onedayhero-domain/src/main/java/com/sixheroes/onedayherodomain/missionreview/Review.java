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

    @Column(name = "star_id", nullable = false)
    private Long starId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;
}
