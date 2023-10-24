package com.sixheroes.onedayherodomain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", length = 20, nullable = false)
    private UserSocialType userSocialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private UserRole userRole;

    @Column(name = "introduce", length = 200, nullable = false)
    private String introduce;

    @Column(name = "hero_score", columnDefinition = "SMALLINT", nullable = false)
    private Integer heroScore;

    @Column(name = "favorite_date", length = 45, nullable = false)
    private String favoriteDate;

    @Column(name = "is_hero_mode", nullable = false)
    private Boolean isHeroMode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}