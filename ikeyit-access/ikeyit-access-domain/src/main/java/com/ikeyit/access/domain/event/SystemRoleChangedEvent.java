package com.ikeyit.access.domain.event;

import com.ikeyit.access.domain.model.SystemRole;
import com.ikeyit.common.data.domain.BaseDomainEvent;

public class SystemRoleChangedEvent extends BaseDomainEvent {
    private final SystemRole role;

    public SystemRoleChangedEvent(SystemRole role) {
        this.role = role;
    }

    public SystemRole getRole() {
        return role;
    }
}
