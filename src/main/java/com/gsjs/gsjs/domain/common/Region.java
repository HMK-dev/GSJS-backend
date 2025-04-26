package com.gsjs.gsjs.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {
    SEOUL("서울"),
    GYEONGGI("경기도"),
    INCHEON("인천"),
    BUSAN("부산"),
    DAEGU("대구"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GANGWON("강원도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    JEONBUK("전라북도"),
    JEONNAM("전라남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEJU("제주도");

    private final String name;

    public static Region fromName(String name) {
        return java.util.Arrays.stream(values())
                .filter(region -> region.name.equals(name))
                .findFirst()
                //todo implement exception
                .orElseThrow(() -> new IllegalArgumentException("Invalid Region name: " + name));
    }

}
