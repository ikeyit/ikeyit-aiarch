package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserEmailUpdatedEvent extends BaseUserEvent {

    private String previousEmail;

    @ForRepo
    UserEmailUpdatedEvent() {
    }

    public UserEmailUpdatedEvent(User user, String previousEmail) {
        super(user);
        this.previousEmail = previousEmail;
    }

    public String getPreviousEmail() {
        return previousEmail;
    }
}
