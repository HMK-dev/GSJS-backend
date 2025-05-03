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
                @Index(name = "idx_company_bizNo", columnList = "bizNo"),
        }
)
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String bizNo; // 사업자등록번호

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    private String industryDetail;

    @Enumerated(EnumType.STRING)
    private Region region;

    private String address;

    private String postalCode;

    private String websiteUrl;

    private Integer establishedYear;

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
    public static Company create(String name, String bizNo, String address, Industry industry, String industryDetail,
                                 Region region, String postalCode) {
        return Company.builder()
                .name(name)
                .bizNo(bizNo)
                .postalCode(postalCode)
                .industryDetail(industryDetail)
                .address(address)
                .industry(industry)
                .region(region)
                .build();
    }

    public void addAnnualData(AnnualData annualData) {
        this.annualDataList.add(annualData);
        annualData.setCompany(this);
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void addViewCount(int count) {
        this.viewCount += count;
    }

    //update
    public void updateDescription(String description) {
        this.description = description;
    }


    public void updateWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void updateEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }

    //validate
}
