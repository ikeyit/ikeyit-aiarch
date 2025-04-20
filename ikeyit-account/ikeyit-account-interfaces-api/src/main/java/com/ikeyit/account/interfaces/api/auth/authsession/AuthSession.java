package com.ikeyit.account.interfaces.api.auth.authsession;

import com.ikeyit.account.infrastructure.security.UserPrincipal;

import java.time.Duration;
import java.time.Instant;

public class AuthSession {
    private String id;
    private UserPrincipal principal;
    private String method;
    private Instant createdAt;
    private Instant expiresAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserPrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(UserPrincipal principal) {
        this.principal = principal;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Duration duration() {
        return Duration.between(createdAt, expiresAt);
    }
}
