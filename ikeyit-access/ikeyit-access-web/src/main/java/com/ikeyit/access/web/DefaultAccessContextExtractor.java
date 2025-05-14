package com.ikeyit.access.web;

import jakarta.servlet.http.HttpServletRequest;

public class DefaultAccessContextExtractor implements AccessContextExtractor {

    private String realmIdHeaderName = "X_REALM_ID";

    private String realmTypeHeaderName = "X_REALM_TYPE";;

    private String defaultRealmType;

    public DefaultAccessContextExtractor() {
    }

    public DefaultAccessContextExtractor(String defaultRealmType) {
        this.defaultRealmType = defaultRealmType;
    }

    public void setRealmIdHeaderName(String realmIdHeaderName) {
        this.realmIdHeaderName = realmIdHeaderName;
    }

    public void setRealmTypeHeaderName(String realmTypeHeaderName) {
        this.realmTypeHeaderName = realmTypeHeaderName;
    }

    public void setDefaultRealmType(String defaultRealmType) {
        this.defaultRealmType = defaultRealmType;
    }

    @Override
    public AccessContext extract(HttpServletRequest request) {
        String realmId = request.getHeader(realmIdHeaderName);
        if (realmId == null)
            return null;
        String realmType = request.getHeader(realmTypeHeaderName);
        if (realmType == null){
            realmType = defaultRealmType;
        }
        if (realmType == null)
            return null;
        return new AccessContext( Long.parseLong(realmId), realmType);
    }
}
