package com.ikeyit.account.application.model;

public class VerifySignupResultDTO {
    private String message;

    public VerifySignupResultDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
