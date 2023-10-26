package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private UserBasicInfo userBasicInfo;

    @Embedded
    private UserFavoriteWorkingDay userFavoriteWorkingDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", length = 20, nullable = false)
    private UserSocialType userSocialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private UserRole userRole;

    @Column(name = "hero_score", columnDefinition = "SMALLINT", nullable = false)
    private Integer heroScore;

    @Column(name = "is_hero_mode", nullable = false)
    private Boolean isHeroMode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Builder
    private User(
        Email email,
        UserBasicInfo userBasicInfo,
        UserFavoriteWorkingDay userFavoriteWorkingDay,
        UserSocialType userSocialType,
        UserRole userRole
    ) {
        this.email = email;
        this.userBasicInfo = userBasicInfo;
        this.userFavoriteWorkingDay = userFavoriteWorkingDay;
        this.userSocialType = userSocialType;
        this.userRole = userRole;
        this.heroScore = 30;
        this.isHeroMode = false;
        this.isActive = true;
    }

    public void updateUser(
        UserBasicInfo userBasicInfo,
        UserFavoriteWorkingDay userFavoriteWorkingDay
    ) {
        this.userBasicInfo = userBasicInfo;
        this.userFavoriteWorkingDay = userFavoriteWorkingDay;
    }
}