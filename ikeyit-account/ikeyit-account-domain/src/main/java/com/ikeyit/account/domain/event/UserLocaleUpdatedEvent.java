package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserLocaleUpdatedEvent extends BaseUserEvent {

    @ForRepo
    UserLocaleUpdatedEvent() {
    }

    public UserLocaleUpdatedEvent(User user) {
        super(user);
    }
} 