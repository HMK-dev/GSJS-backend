package com.gsjs.gsjs.domain.aiRecommendation.entity;

import com.gsjs.gsjs.domain.aiRecommendationCompany.entity.AiRecommendationCompany;
import com.gsjs.gsjs.domain.auditing.entity.BaseTimeEntity;
import com.gsjs.gsjs.domain.member.entity.Member;
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
public class AiRecommendation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String recommendationReason;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiRecommendationCompany> recommendedCompanies = new ArrayList<>();

}
