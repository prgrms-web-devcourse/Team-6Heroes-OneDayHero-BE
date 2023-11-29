package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_activity_statistics")
@Entity
public class UserActivityStatistic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "complete_count", nullable = false)
    private Integer completeCount;

    @Column(name = "no_show_count", nullable = false)
    private Integer noShowCount;

    @Column(name = "give_up_count", nullable = false)
    private Integer giveUpCount;

    @Column(name = "withdraw_count", nullable = false)
    private Integer withdrawCount;

    @Builder
    private UserActivityStatistic(
        User user
    ) {
        this.user = user;
        this.completeCount = 0;
        this.noShowCount = 0;
        this.giveUpCount = 0;
        this.withdrawCount = 0;
    }
}