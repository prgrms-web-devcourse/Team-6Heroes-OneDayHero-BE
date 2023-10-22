package com.sixheroes.onedayherodomain.region;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(name = "si", nullable = false)
    private String si;

    @Column(name = "gu", nullable = false)
    private String gu;

    @Column(name = "dong", nullable = false)
    private String dong;
}
