package com.ikeyit.account.domain.event;

import com.ikeyit.account.domain.model.Gender;
import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.data.domain.BaseDomainEvent;
import com.ikeyit.common.data.domain.ForRepo;

public class BaseUserEvent extends BaseDomainEvent {
    private Long userId;
    private String username;
    private String email;
    private String mobile;
    private String displayName;
    private String location;
    private String avatar;
    private String locale;
    private Gender gender;
    private boolean enabled;
    private boolean verified;

    @ForRepo
    BaseUserEvent() {
    }

    public BaseUserEvent(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
        this.locale = user.getLocale();
        this.displayName = user.getDisplayName();
        this.avatar = user.getAvatar();
        this.location = user.getLocation();
        this.gender = user.getGender();
        this.enabled = user.isEnabled();
        this.verified = user.isVerified();
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLocale() {
        return locale;
    }

    public Gender getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVerified() {
        return verified;
    }
}
