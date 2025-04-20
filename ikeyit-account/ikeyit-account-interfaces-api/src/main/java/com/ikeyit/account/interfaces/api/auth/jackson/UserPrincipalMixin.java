package com.ikeyit.account.interfaces.api.auth.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class UserPrincipalMixin {

    @JsonIgnore
    abstract Set<SimpleGrantedAuthority> getAuthorities();
}
