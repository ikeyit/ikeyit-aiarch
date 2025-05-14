package com.ikeyit.access.core;

public interface AccessService {
    AccessDecision check(AccessRequest accessRequest);
    GrantedAuthorities getAuthorities(AuthoritiesRequest authoritiesRequest);
}
