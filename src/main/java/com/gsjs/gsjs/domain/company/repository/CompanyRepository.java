package com.gsjs.gsjs.domain.company.repository;

import com.gsjs.gsjs.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
