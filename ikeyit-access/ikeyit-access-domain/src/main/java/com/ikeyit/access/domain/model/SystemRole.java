package com.ikeyit.access.domain.model;


import com.ikeyit.access.domain.event.SystemRoleChangedEvent;
import com.ikeyit.common.data.JsonField;
import com.ikeyit.common.data.domain.BaseAggregateRoot;

import java.util.SortedSet;
import java.util.TreeSet;

public class SystemRole extends BaseAggregateRoot<Long> {
    private Long id;
    private String realmType;
    private String name;
    private boolean defaultRole;
    private boolean supreme;
    @JsonField
    private SortedSet<Long> permissionIds;

    public SystemRole() {
    }

    public SystemRole(String realmType, String name) {
        this.realmType = realmType;
        this.name = name;
        this.permissionIds = new TreeSet<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void assignId(Long id) {
        this.id = id;
    }

    public boolean isDefaultRole() {
        return defaultRole;
    }

    public String getName() {
        return name;
    }

    public String getRealmType() {
        return realmType;
    }

    public SortedSet<Long> getPermissionIds() {
        return permissionIds;
    }

    public boolean isSupreme() {
        return supreme;
    }

    public void addedPermissionId(Long permissionId) {
        permissionIds.add(permissionId);
        this.addDomainEvent(new SystemRoleChangedEvent(this));
    }
}
