package com.ikeyit.account.application.model;

public class UpdateUserLocaleCMD {
    private Long userId;

    private String locale;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
