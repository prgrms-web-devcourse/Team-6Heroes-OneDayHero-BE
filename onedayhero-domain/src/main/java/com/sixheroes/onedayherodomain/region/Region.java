package com.sixheroes.onedayherodomain.region;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "regions")
@Entity
public class Region {

    @Id
    private Long id;

    @Column(name = "si", length = 10, nullable = false)
    private String si;

    @Column(name = "gu", length = 10, nullable = false)
    private String gu;

    @Column(name = "dong", length = 10, nullable = false)
    private String dong;

    @Builder
    private Region(
            Long id,
            String si,
            String gu,
            String dong
    ) {
        this.id = id;
        this.si = si;
        this.gu = gu;
        this.dong = dong;
    }
}
