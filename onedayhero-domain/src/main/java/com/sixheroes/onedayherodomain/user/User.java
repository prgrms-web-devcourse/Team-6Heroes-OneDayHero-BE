package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
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

    @OneToMany(mappedBy = "user")
    private List<UserImage> userImages = new ArrayList<>();

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

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
  
    //처음 Oauth 를 통해 회원 가입
    public static User singUpUser(
            Email email,
            UserSocialType userSocialType,
            UserRole userRole
    ) {
        return new User(
                email,
                userSocialType,
                userRole);
    }

    private User(
            Email email,
            UserSocialType userSocialType,
            UserRole userRole) {
        this.email = email;
        this.userSocialType = userSocialType;
        this.userRole = userRole;
        this.heroScore = 30;
        this.isHeroMode = false;
    }

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
        this.isDeleted = false;
    }

    protected void setUserImages(
        UserImage userImage
    ) {
        this.userImages.add(userImage);
    }

    public void updateUser(
        UserBasicInfo userBasicInfo,
        UserFavoriteWorkingDay userFavoriteWorkingDay
    ) {
        this.userBasicInfo = userBasicInfo;
        this.userFavoriteWorkingDay = userFavoriteWorkingDay;
    }
  
    public void changeHeroModeOn() {
        validHeroModeOff();
        this.isHeroMode = true;
    }

    public void changeHeroModeOff() {
        validHeroModeOn();
        this.isHeroMode = false;
    }

    public void validPossibleHeroProfile() {
        validHeroModeOn();
    }

    public void validPossibleMissionProposal() {
        validHeroModeOn();
    }

    private void validHeroModeOn() {
        if (Boolean.FALSE.equals(this.isHeroMode)) {
            log.debug("해당 유저는 히어로 모드가 비활성화 상태입니다.");
            throw new IllegalStateException(ErrorCode.EU_009.name());
        }
    }

    private void validHeroModeOff() {
        if (Boolean.TRUE.equals(this.isHeroMode)) {
            log.debug("해당 유저는 히어로 모드가 활성화 상태입니다.");
            throw new IllegalStateException(ErrorCode.EU_010.name());
        }
    }
}