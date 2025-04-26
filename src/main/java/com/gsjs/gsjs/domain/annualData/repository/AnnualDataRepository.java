package com.gsjs.gsjs.domain.annualData.repository;

import com.gsjs.gsjs.domain.annualData.entity.AnnualData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnualDataRepository extends JpaRepository<AnnualData, Long> {
}
