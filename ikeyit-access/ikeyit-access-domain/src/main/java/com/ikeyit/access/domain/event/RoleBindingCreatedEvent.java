package com.ikeyit.access.domain.event;

import com.ikeyit.access.domain.model.RoleBinding;
import com.ikeyit.access.domain.model.RoleType;
import com.ikeyit.common.data.domain.BaseDomainEvent;

public class RoleBindingCreatedEvent extends BaseDomainEvent {
    private Long userId;
    private Long roleId;
    private RoleType roleType;
    private Long realmId;
    private String realmType;

    public RoleBindingCreatedEvent() {
    }

    public RoleBindingCreatedEvent(RoleBinding roleBinding) {
        this.userId = roleBinding.getUserId();
        this.roleId = roleBinding.getRoleId();
        this.roleType = roleBinding.getRoleType();
        this.realmId = roleBinding.getRealmId();
        this.realmType = roleBinding.getRealmType();
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public Long getRealmId() {
        return realmId;
    }

    public String getRealmType() {
        return realmType;
    }
}
