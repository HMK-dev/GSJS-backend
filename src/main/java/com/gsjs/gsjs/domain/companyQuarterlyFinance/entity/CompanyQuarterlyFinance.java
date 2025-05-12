package com.gsjs.gsjs.domain.companyQuarterlyFinance.entity;

import com.gsjs.gsjs.domain.company.entity.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class CompanyQuarterlyFinance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private Integer year; // format-ex: 1990, 2000

    @Min(value = 1)
    @Max(value = 4)
    private Integer quarter; // 분기 format-ex: 1 ~ 4

    // 재무 데이터
    private BigDecimal revenue;           // 매출액
    private BigDecimal companyValue;      // 기업 가치
    private BigDecimal growthRate;        // 성장률

    //business
    public static CompanyQuarterlyFinance create(Integer year, Integer quarter,
                                                 BigDecimal revenue, BigDecimal companyValue, BigDecimal growthRate) {
        return CompanyQuarterlyFinance.builder()
                .year(year)
                .quarter(quarter)
                .revenue(revenue)
                .companyValue(companyValue)
                .growthRate(growthRate)
                .build();
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
