package com.gsjs.gsjs.domain.company.entity;

import com.gsjs.gsjs.domain.auditing.entity.BaseTimeEntity;
import com.gsjs.gsjs.domain.common.Industry;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Lob
    private String description;

    //todo enum
    private Industry industry;

    //todo enum
    private Region region;

    private String address;

    private String websiteUrl;

    private Integer establishedYear; //format-ex: 1990, 2000

    private int viewCount;

    //todo relationship
    /** todo
     * 1.annualData
     * 2. jobPostings
     * 3. scraps
     *
     */

}
