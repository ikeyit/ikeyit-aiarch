package com.ikeyit.access.application.model;

import java.util.List;

public class UpdateMemberRolesCMD {
    private String realmType;

    private Long realmId;

    private Long userId;

    private List<MemberRoleCMD> roles;

    private boolean appended;

    public String getRealmType() {
        return realmType;
    }

    public void setRealmType(String realmType) {
        this.realmType = realmType;
    }

    public Long getRealmId() {
        return realmId;
    }

    public void setRealmId(Long realmId) {
        this.realmId = realmId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<MemberRoleCMD> getRoles() {
        return roles;
    }

    public void setRoles(List<MemberRoleCMD> roles) {
        this.roles = roles;
    }

    public boolean isAppended() {
        return appended;
    }

    public void setAppended(boolean appended) {
        this.appended = appended;
    }
}
