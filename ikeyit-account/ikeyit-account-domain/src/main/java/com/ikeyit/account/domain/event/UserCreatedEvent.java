package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserCreatedEvent extends BaseUserEvent {

    @ForRepo
    UserCreatedEvent() {
    }

    public UserCreatedEvent(User user) {
        super(user);
    }

}
