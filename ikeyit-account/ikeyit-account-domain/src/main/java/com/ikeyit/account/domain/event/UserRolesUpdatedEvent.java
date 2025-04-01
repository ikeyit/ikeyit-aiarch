package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserRolesUpdatedEvent extends BaseUserEvent {

    @ForRepo
    UserRolesUpdatedEvent() {
    }

    public UserRolesUpdatedEvent(User user) {
        super(user);
    }
} 