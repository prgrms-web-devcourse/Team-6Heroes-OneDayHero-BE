package com.sixheroes.onedayherodomain.user;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private UserSocialType userSocialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole userRole;

    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "hero_score", nullable = false)
    private Integer heroScore;

    @Type(JsonType.class)
    @Column(name = "favorite_date", columnDefinition = "json", nullable = false)
    private Map<String, Week[]> favoriteDate = new HashMap<>();

    @Column(name = "is_hero_mode", nullable = false)
    private Boolean isHeroMode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}