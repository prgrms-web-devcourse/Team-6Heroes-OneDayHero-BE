package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_images")
@Entity
public class UserImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "original_name", length = 260, nullable = false)
    private String originalName;

    @Column(name = "unique_name", length = 100, nullable = false)
    private String uniqueName;

    @Column(name = "path", length = 250, nullable = false)
    private String path;

    @Builder(access = AccessLevel.PRIVATE)
    private UserImage(
            @NonNull User user,
            String originalName,
            String uniqueName,
            String path
    ) {
        validCreateUserImage(originalName, uniqueName, path);

        this.user = user;
        this.originalName = originalName;
        this.uniqueName = uniqueName;
        this.path = path;
    }

    public static UserImage createUserImage(
            @NonNull User user,
            String originalName,
            String uniqueName,
            String path
    ) {
        var userImage = UserImage.builder()
                .user(user)
                .originalName(originalName)
                .uniqueName(uniqueName)
                .path(path)
                .build();

        user.setUserImage(userImage);
        return userImage;
    }

    private void validCreateUserImage(
            String originalName,
            String uniqueName,
            String path
    ) {
        validOriginalNameLength(originalName);
        validUniqueNameLength(uniqueName);
        validPathLength(path);
    }

    private void validOriginalNameLength(String originalName) {
        if (originalName.length() > 260) {
            log.debug("original name이 260자를 초과했습니다. originalName.length() : {}", originalName.length());
            throw new IllegalArgumentException(ErrorCode.EI_001.name());
        }
    }

    private void validUniqueNameLength(String uniqueName) {
        if (uniqueName.length() > 100) {
            log.debug("unique name이 100자를 초과했습니다. uniqueName.length() : {}", uniqueName.length());
            throw new IllegalArgumentException(ErrorCode.EI_002.name());
        }
    }

    private void validPathLength(String path) {
        if (path.length() > 250) {
            log.debug("path가 250자를 초과했습니다. path.length() : {}", path.length());
            throw new IllegalArgumentException(ErrorCode.EI_003.name());
        }
    }
}