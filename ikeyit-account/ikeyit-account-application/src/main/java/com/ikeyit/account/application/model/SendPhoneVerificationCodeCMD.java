package com.ikeyit.account.application.model;


public class SendPhoneVerificationCodeCMD {

    private Long userId;

    private String phone;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
