package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserEnabledEvent extends BaseUserEvent {

    @ForRepo
    UserEnabledEvent() {
    }

    public UserEnabledEvent(User user) {
        super(user);
    }
} 