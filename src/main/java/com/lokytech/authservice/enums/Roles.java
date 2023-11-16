package com.lokytech.authservice.enums;

public enum Roles {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_GUEST;

    public static Roles fromString(String roles){
        return Roles.valueOf(roles.toUpperCase());
    }
}
