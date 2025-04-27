package com.gsjs.gsjs.domain.common;

import com.gsjs.gsjs.exception.object.domain.IndustryHandler;
import com.gsjs.gsjs.exception.payload.code.ErrorStatus;
import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

@Getter
@RequiredArgsConstructor
public enum Industry {
    //변경가능
    MANUFACTURING("제조업"),
    IT_COMMUNICATION("정보통신업"),
    PROFESSIONAL_SCIENTIFIC("전문, 과학 및 기술 서비스업"),
    WHOLESALE_RETAIL("도매 및 소매업"),
    TRANSPORTATION_STORAGE("운수 및 창고업"),
    CONSTRUCTION("건설업"),
    BUSINESS_FACILITY("사업시설 관리, 사업 지원 및 임대 서비스업"),
    ENVIRONMENTAL("수도, 하수 및 폐기물 처리, 원료 재생업"),
    HEALTHCARE_WELFARE("보건업 및 사회복지 서비스업"),
    EDUCATION("교육 서비스업"),
    UTILITY("전기, 가스, 증기 및 공기조절 공급업"),
    ASSOCIATION_REPAIR("협회 및 단체, 수리 및 기타 개인 서비스업"),
    ACCOMMODATION_FOOD("숙박 및 음식점업"),
    REAL_ESTATE("부동산업"),
    ARTS_SPORTS("예술, 스포츠 및 여가관련 서비스업"),
    FINANCE_INSURANCE("금융 및 보험업"),
    AGRICULTURE_FORESTRY("농업, 임업 및 어업"),
    MINING("광업"),
    OTHER("기타"),
    ;

    private final String name;
    private static final Double SIMILARITY_THRESHOLD = 0.90; // 90% 이상 유사도


    public static Industry fromName(String name) {
        return java.util.Arrays.stream(values())
                .map(industry -> new java.util.AbstractMap.SimpleEntry<>(industry, calculateSimilarity(industry.name(), name)))
                .filter(entry -> entry.getValue() >= SIMILARITY_THRESHOLD)
                .max(Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElseThrow(() -> new IndustryHandler(ErrorStatus.INDUSTRY_NOT_FOUND));
    }


    private static double calculateSimilarity(String str1, String str2) {
        // Levenshtein distance 기반 유사도 계산
        int distance = StringUtils.getLevenshteinDistance(str1, str2);
        int maxLength = Math.max(str1.length(), str2.length());

        // 유사도 계산: (최대 길이 - 편집 거리) / 최대 길이
        return 1.0 - (double) distance / maxLength;
    }
}