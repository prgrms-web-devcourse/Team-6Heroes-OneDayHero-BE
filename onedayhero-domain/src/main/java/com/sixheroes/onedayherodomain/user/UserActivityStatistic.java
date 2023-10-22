package com.sixheroes.onedayherodomain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_activity_statistics")
@Entity
public class UserActivityStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "complete_count", nullable = false)
    private Integer completeCount;

    @Column(name = "no_show_count", nullable = false)
    private Integer noShowCount;

    @Column(name = "give_up_count", nullable = false)
    private Integer giveUpCount;

    @Column(name = "withdraw_count", nullable = false)
    private Integer withdrawCount;
}
