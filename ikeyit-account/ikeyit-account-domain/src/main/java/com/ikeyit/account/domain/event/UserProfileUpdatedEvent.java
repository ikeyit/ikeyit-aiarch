package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserProfileUpdatedEvent extends BaseUserEvent {

    @ForRepo
    UserProfileUpdatedEvent() {
    }

    public UserProfileUpdatedEvent(User user) {
        super(user);
    }
}
