package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_regions", uniqueConstraints = {
    @UniqueConstraint(
        columnNames = { "user_id", "region_id" }
    )
})
@Entity
public class UserRegion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "region_id", nullable = false)
    private Long regionId;

    @Builder
    private UserRegion(
        User user,
        Long regionId
    ) {
        this.user = user;
        this.regionId = regionId;
    }
}
