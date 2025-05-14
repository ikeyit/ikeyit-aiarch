package com.ikeyit.access.domain.model;


import com.ikeyit.access.domain.event.CustomRoleCreatedEvent;
import com.ikeyit.access.domain.event.CustomRoleUpdatedEvent;
import com.ikeyit.common.data.JsonField;
import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.exception.BizAssert;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class CustomRole extends BaseAggregateRoot<Long> {
    private Long id;

    private String realmType;

    private Long realmId;

    private String name;

    @JsonField
    private SortedSet<Long> permissionIds;

    public CustomRole() {
    }

    public CustomRole(String realmType, Long realmId, String name, Collection<Long> permissionIds) {
        BizAssert.notEmpty(realmType, "realmType is required");
        BizAssert.notNull(realmId, "realmId is required");
        BizAssert.notEmpty(name, "name is required");
        BizAssert.notNull(permissionIds, "permissionIds is required");
        this.realmType = realmType;
        this.realmId = realmId;
        this.name = name;
        this.permissionIds = new TreeSet<>(permissionIds);
        this.addDomainEvent(() -> new CustomRoleCreatedEvent(this));
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void assignId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getRealmType() {
        return realmType;
    }

    public Collection<Long> getPermissionIds() {
        return permissionIds;
    }

    public Long getRealmId() {
        return realmId;
    }

    public void update(String name, Collection<Long> permissionIds) {
        BizAssert.notEmpty(name, "name is empty");
        BizAssert.notEmpty(permissionIds, "permissionIds is empty");
        this.name = name;
        this.permissionIds = new TreeSet<>(permissionIds);
        this.addDomainEvent(new CustomRoleUpdatedEvent(this));
    }

}
