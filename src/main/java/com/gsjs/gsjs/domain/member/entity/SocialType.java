package com.gsjs.gsjs.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GOOGLE("google"), NAVER("naver");

    private final String name;

    public static SocialType fromName(String name) {
        return java.util.Arrays.stream(values())
                .filter(socialType -> socialType.name.equals(name))
                .findFirst()
                //todo implement exception
                .orElseThrow(() -> new IllegalArgumentException("Invalid SocialType name: " + name));
    }
}