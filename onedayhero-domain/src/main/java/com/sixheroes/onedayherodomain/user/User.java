package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User {

    private static final String EMAIL_REGEX = "\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\b";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(name = "email", length = 255, nullable = false)
    private Email email;

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

    @Convert(converter = FavoriteDateConverter.class)
    @Column(name = "favorite_date", length = 45, nullable = false)
    private List<Week> favoriteDate;

    @Column(name = "is_hero_mode", nullable = false)
    private Boolean isHeroMode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Builder
    public User(
        Email email,
        LocalDate birth,
        UserSocialType userSocialType,
        UserRole userRole,
        String introduce,
        List<Week> favoriteDate
    ) {
        validCreateUser(introduce);

        this.email = email;
        this.birth = birth;
        this.userSocialType = userSocialType;
        this.userRole = userRole;
        this.introduce = introduce;
        this.heroScore = 30;
        this.favoriteDate = favoriteDate;
        this.isHeroMode = false;
        this.isActive = true;
    }

    private void validCreateUser(
        String introduce
    ) {
        validIntroduceNotBlank(introduce);
        validIntroduceLength(introduce);
    }

    private void validIntroduceNotBlank(
        String introduce
    ) {
        if (!StringUtils.hasText(introduce)) {
            log.debug("introduce가 빈 값이면 안됩니다.");
            throw new IllegalArgumentException(ErrorCode.U_001.name());
        }
    }

    private void validIntroduceLength(String introduce) {
        if (introduce.length() > 200) {
            log.debug("introduce 길이가 200을 초과하였습니다. introduce.length() : {}", introduce.length());
            throw new IllegalArgumentException(ErrorCode.U_001.name());
        }
    }
}