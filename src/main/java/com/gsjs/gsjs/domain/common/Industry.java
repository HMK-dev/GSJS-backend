package com.gsjs.gsjs.domain.common;

import com.gsjs.gsjs.exception.object.domain.IndustryHandler;
import com.gsjs.gsjs.exception.payload.code.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Industry {
    //변경가능
    MANUFACTURING("제조업"),
    IT("정보통신업"),
    BIOTECH("바이오/의료"),
    SERVICE("서비스업"),
    FINANCE("금융업"),
    CONSTRUCTION("건설업"),
    ENERGY("에너지/환경"),
    RETAIL("유통/물류"),
    EDUCATION("교육업"),
    OTHERS("기타");

    private final String name;

    public static Industry fromName(String name) {
        return java.util.Arrays.stream(values())
                .filter(industry -> industry.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IndustryHandler(ErrorStatus.INDUSTRY_NOT_FOUND));
    }

}