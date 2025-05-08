package com.gsjs.gsjs.domain.company.repository;

import com.gsjs.gsjs.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("select c from Company c where c.bizNo = :bizNo")
    Optional<Company> findByBizNo(@Param("bizNo") String bizNo);

}
