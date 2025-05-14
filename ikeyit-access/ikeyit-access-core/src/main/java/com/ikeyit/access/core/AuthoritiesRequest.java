package com.ikeyit.access.core;

public class AuthoritiesRequest {
    private Long userId;
    private Long realmId;
    private String realmType;

    public AuthoritiesRequest() {
    }

    public AuthoritiesRequest(Long userId, Long realmId, String realmType) {
        this.userId = userId;
        this.realmId = realmId;
        this.realmType = realmType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRealmId() {
        return realmId;
    }

    public void setRealmId(Long realmId) {
        this.realmId = realmId;
    }

    public String getRealmType() {
        return realmType;
    }

    public void setRealmType(String realmType) {
        this.realmType = realmType;
    }
}
