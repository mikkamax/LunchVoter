package com.mike.lunchvoter.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(Permission.values())),
    USER(Set.of(
            Permission.PROFILE_READ,
            Permission.PROFILE_UPDATE,
            Permission.PROFILE_DELETE,
            Permission.RESTAURANT_VOTE
    ));

    private final Set<Permission> permissions;

    public Set<GrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
