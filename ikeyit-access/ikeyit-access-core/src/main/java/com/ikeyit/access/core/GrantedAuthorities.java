package com.ikeyit.access.core;

import java.util.Collection;
import java.util.Set;

public class GrantedAuthorities {
    private Long memberId = 0L;
    private String memberDisplayName = "";
    private final Set<String> roles;
    private final Set<String> permissions;

    public GrantedAuthorities() {
        roles = Set.of();
        permissions = Set.of();
    }

    public GrantedAuthorities(Long memberId, String memberDisplayName, Collection<String> roles, Collection<String> permissions) {
        this.memberId = memberId;
        this.memberDisplayName = memberDisplayName;
        this.roles = Set.copyOf(roles);
        this.permissions = Set.copyOf(permissions);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberDisplayName() {
        return memberDisplayName;
    }

}
