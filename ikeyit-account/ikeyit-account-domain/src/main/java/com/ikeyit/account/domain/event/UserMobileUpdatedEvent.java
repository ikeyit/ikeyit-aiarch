package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.ForRepo;

public class UserMobileUpdatedEvent extends BaseUserEvent {

    private String previousMobile;

    @ForRepo
    UserMobileUpdatedEvent() {

    }

    public UserMobileUpdatedEvent(User user,String previousMobile) {
        super(user);
        this.previousMobile = previousMobile;
    }

    public String getPreviousMobile() {
        return previousMobile;
    }
}
