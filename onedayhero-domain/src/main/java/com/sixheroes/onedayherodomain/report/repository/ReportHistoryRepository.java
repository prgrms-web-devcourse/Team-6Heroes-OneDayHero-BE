package com.sixheroes.onedayherodomain.report.repository;

import com.sixheroes.onedayherodomain.report.ReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportHistoryRepository extends JpaRepository<ReportHistory, Long> {
}
