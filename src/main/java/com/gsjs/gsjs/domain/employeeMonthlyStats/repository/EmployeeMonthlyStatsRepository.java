package com.gsjs.gsjs.domain.employeeMonthlyStats.repository;

import com.gsjs.gsjs.domain.employeeMonthlyStats.entity.EmployeeMonthlyStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeMonthlyStatsRepository extends JpaRepository<EmployeeMonthlyStats, Long> {
}
