package com.ikeyit.access.web;

public class AccessContext {

    private final Long realmId;

    private final String realmType;

    public AccessContext(Long realmId, String realmType) {
        this.realmId = realmId;
        this.realmType = realmType;
    }

    public Long getRealmId() {
        return realmId;
    }

    public String getRealmType() {
        return realmType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccessContext{");
        sb.append("realmId=").append(realmId);
        sb.append(", realmType='").append(realmType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
