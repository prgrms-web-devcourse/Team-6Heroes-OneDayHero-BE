package com.sixheroes.onedayherodomain.region;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "regions")
@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "si", length = 10, nullable = false)
    private String si;

    @Column(name = "gu", length = 10, nullable = false)
    private String gu;

    @Column(name = "dong", length = 10, nullable = false)
    private String dong;

    @Builder
    private Region(
        String si,
        String gu,
        String dong
    ) {
        this.si = si;
        this.gu = gu;
        this.dong = dong;
    }
}
