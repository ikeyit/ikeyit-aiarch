package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserPasswordUpdatedEvent extends BaseUserEvent {

    @ForRepo
    UserPasswordUpdatedEvent() {

    }

    public UserPasswordUpdatedEvent(User user) {
        super(user);
    }
}