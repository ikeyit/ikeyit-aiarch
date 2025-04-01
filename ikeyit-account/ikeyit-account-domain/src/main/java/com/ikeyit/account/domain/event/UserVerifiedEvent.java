package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserVerifiedEvent extends BaseUserEvent {

    @ForRepo
    UserVerifiedEvent() {
    }

    public UserVerifiedEvent(User user) {
        super(user);
    }
} 