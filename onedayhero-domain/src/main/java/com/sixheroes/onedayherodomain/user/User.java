package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
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
import java.util.Objects;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "nickname")
    }
)
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

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
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

    public static User signUp(
            Email email,
            UserSocialType userSocialType,
            UserRole userRole,
            UserBasicInfo userBasicInfo
    ) {
        return User.builder()
                .email(email)
                .userSocialType(userSocialType)
                .userRole(userRole)
                .userBasicInfo(userBasicInfo)
                .userFavoriteWorkingDay(null)
                .build();
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

    protected void setUserImage(
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

    protected void validOwner(
            Long userId
    ) {
        if (!Objects.equals(this.id, userId)) {
            log.debug("유저의 아이디가 일치하지 않습니다. id = {}, userId = {}", this.id, userId);
            throw new BusinessException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
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
            throw new BusinessException(ErrorCode.HERO_MODE_OFF);
        }
    }

    private void validHeroModeOff() {
        if (Boolean.TRUE.equals(this.isHeroMode)) {
            log.debug("해당 유저는 히어로 모드가 활성화 상태입니다.");
            throw new BusinessException(ErrorCode.HERO_MODE_ON);
        }
    }
}
