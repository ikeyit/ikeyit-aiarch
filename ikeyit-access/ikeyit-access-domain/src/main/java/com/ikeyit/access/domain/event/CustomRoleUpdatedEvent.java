package com.ikeyit.access.domain.event;

import com.ikeyit.access.domain.model.CustomRole;
import com.ikeyit.common.data.domain.BaseDomainEvent;

public class CustomRoleUpdatedEvent extends BaseDomainEvent {
    private Long roleId;

    private String name;
    public CustomRoleUpdatedEvent() {}
    public CustomRoleUpdatedEvent(CustomRole role) {
        this.roleId = role.getId();
        this.name = role.getName();
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }
}
