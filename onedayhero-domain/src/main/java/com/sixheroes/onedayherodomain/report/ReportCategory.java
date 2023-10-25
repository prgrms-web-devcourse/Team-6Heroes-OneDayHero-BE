package com.sixheroes.onedayherodomain.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report_categories")
@Entity
public class ReportCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", length = 20, nullable = false)
    private ReportCategoryCode reportCategoryCode;

    @Column(name = "name", length = 50, nullable = false)
    private String name;
}
