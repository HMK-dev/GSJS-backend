package com.gsjs.gsjs.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String name;

    public static Role fromName(String name) {
        return java.util.Arrays.stream(values())
                .filter(role -> role.name.equals(name))
                .findFirst()
                //todo implement exception
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role name: " + name));
    }

}
