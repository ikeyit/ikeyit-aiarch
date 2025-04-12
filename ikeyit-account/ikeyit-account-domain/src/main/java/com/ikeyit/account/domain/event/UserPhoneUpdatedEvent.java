package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserPhoneUpdatedEvent extends BaseUserEvent {

    private String previousPhone;

    @ForRepo
    UserPhoneUpdatedEvent() {

    }

    public UserPhoneUpdatedEvent(User user,String previousPhone) {
        super(user);
        this.previousPhone = previousPhone;
    }

    public String getPreviousPhone() {
        return previousPhone;
    }
}
