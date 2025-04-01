package com.ikeyit.security.codeauth.core;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * A simple implementation of CodeUserService for testing.
 */
public class InMemoryCodeAuthUserService implements CodeAuthUserService {
    private final String target;
    private final String authority;

    public InMemoryCodeAuthUserService() {
        this("test", "user");
    }

    public InMemoryCodeAuthUserService(String target, String authority) {
        this.target = target;
        this.authority= authority;
    }

    @Override
    public UserDetails loadUserByTarget(String target) {
        if (Objects.equals(this.target, target))
            return User.withUsername(target).password("").authorities(authority).build();
        return null;
    }
}
