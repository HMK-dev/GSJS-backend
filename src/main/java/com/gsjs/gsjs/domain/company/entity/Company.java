package com.gsjs.gsjs.domain.company.entity;

import com.gsjs.gsjs.domain.annualData.entity.AnnualData;
import com.gsjs.gsjs.domain.auditing.entity.BaseTimeEntity;
import com.gsjs.gsjs.domain.common.Industry;
import com.gsjs.gsjs.domain.common.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {
                @Index(name = "idx_company_name", columnList = "name"),
        }
)
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    @Enumerated(EnumType.STRING)
    private Region region;

    private String businessRegistrationNumber; // 사업자등록번호

    private String address;

    private String websiteUrl;

    private Integer establishedYear; //format-ex: 1990, 2000

    private int viewCount = 0;

    //todo relationship
    /** todo
     * 1.annualData
     * 2. jobPostings
     *
     */

    //relation
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnnualData> annualDataList = new ArrayList<>();

    //business
    public Company create(String name, String address, Industry industry, Region region) {
        return Company.builder()
                .name(name)
                .address(address)
                .industry(industry)
                .region(region)
                .build();
    }


    public void addAnnualData(AnnualData annualData) {
        this.annualDataList.add(annualData);
        annualData.setCompany(this);
    }

    public void update(String name, String description, Industry industry, Region region,
                       String address, String websiteUrl, Integer establishedYear) {
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.region = region;
        this.address = address;
        this.websiteUrl = websiteUrl;
        this.establishedYear = establishedYear;
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void addViewCount(int count) {
        this.viewCount += count;
    }

    //validate
}
