package com.mike.lunchvoter.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    PROFILE_READ("profile:read"),
    PROFILE_UPDATE("profile:update"),
    PROFILE_DELETE("profile:delete"),
    RESTAURANT_CREATE("restaurant:create"),
    RESTAURANT_VOTE("restaurant:vote"),
    RESTAURANT_UPDATE("restaurant:update"),
    RESTAURANT_DELETE("restaurant:delete"),
    MENU_CREATE("menu:create"),
    MENU_UPDATE("menu:update"),
    MENU_DELETE("menu:delete");

    private final String permission;

}
