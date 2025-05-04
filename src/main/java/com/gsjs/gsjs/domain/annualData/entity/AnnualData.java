package com.gsjs.gsjs.domain.annualData.entity;

import com.gsjs.gsjs.domain.auditing.entity.BaseTimeEntity;
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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_annual_data_company_year",
                        columnNames = {"company_id", "year"}
                )
        }
)
public class AnnualData extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private Integer year; // format-ex: 1990, 2000

    @Min(value = 1)
    @Max(value = 12)
    private Integer month; // format-ex: 1 ~ 12

    // 국민연금 데이터
    private Integer totalEmployees;       // 국민연금 총 가입자 수
    private Integer newEmployees;         // 신규 가입자 수
    private Integer lostEmployees;    // 탈퇴자 수

    // 재무 데이터
    private BigDecimal revenue;           // 매출액
    private BigDecimal companyValue;      // 기업 가치
    private BigDecimal growthRate;        // 성장률

    // business
    public static AnnualData create(Integer year, Integer month) {
        return AnnualData.builder()
                .year(year)
                .month(month)
                .build();
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void updateEmployeeData(Integer totalEmployees, Integer newEmployees, Integer departedEmployees) {
        this.totalEmployees = totalEmployees;
        this.newEmployees = newEmployees;
        this.lostEmployees = departedEmployees;
    }

    public void updateFinancialData(BigDecimal revenue, BigDecimal companyValue, BigDecimal growthRate) {
        this.revenue = revenue;
        this.companyValue = companyValue;
        this.growthRate = growthRate;
    }

}
