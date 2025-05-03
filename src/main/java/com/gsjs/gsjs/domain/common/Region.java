package com.gsjs.gsjs.domain.common;

import com.gsjs.gsjs.exception.object.domain.RegionHandler;
import com.gsjs.gsjs.exception.payload.code.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    BUSAN("부산"),
    DAEGU("대구"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    JEJU("제주"),
    NONE("NONE")
    ;

    private final String name;

    public static Region fromName(String name) {
        return java.util.Arrays.stream(values())
                .filter(region -> region.name.equals(name))
                .findFirst()
                .orElse(NONE);
    }

}
