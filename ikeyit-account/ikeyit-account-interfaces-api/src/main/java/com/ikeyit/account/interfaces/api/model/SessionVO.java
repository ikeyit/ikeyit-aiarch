package com.ikeyit.account.interfaces.api.model;

public class SessionVO {
    private String csrfToken;
    private UserVO user;
    
    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}
