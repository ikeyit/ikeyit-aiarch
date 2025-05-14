package com.ikeyit.access.application.model;

import java.util.List;

public class CreateCustomRoleCMD {
    private String realmType;
    private Long realmId;
    private String name;
    private List<Long> permissionIds;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
