package com.ikeyit.access.application.model;

import com.ikeyit.access.domain.model.RoleType;

import java.util.List;

public class RoleDTO {
    private Long id;
    private String name;
    private String realmType;
    private RoleType roleType;
    private boolean supreme;
    private List<PermissionDTO> permissions;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealmType() {
        return realmType;
    }

    public void setRealmType(String realmType) {
        this.realmType = realmType;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public boolean isSupreme() {
        return supreme;
    }

    public void setSupreme(boolean supreme) {
        this.supreme = supreme;
    }

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
