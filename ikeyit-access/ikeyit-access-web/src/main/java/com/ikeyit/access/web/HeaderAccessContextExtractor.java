package com.ikeyit.access.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class HeaderAccessContextExtractor implements AccessContextExtractor {

    private String realmType;

    private String headerName;

    public HeaderAccessContextExtractor() {
    }

    public HeaderAccessContextExtractor(String headerName, String realmType) {
        Assert.notNull(headerName, "headerName must not be null");
        Assert.notNull(realmType, "realmType must not be null");
        this.headerName = headerName;
        this.realmType = realmType;

    }

    public void setHeaderName(String headerName) {
        Assert.notNull(headerName, "headerName must not be null");
        this.headerName = headerName;
    }

    public void setRealmType(String realmType) {
        Assert.notNull(realmType, "realmType must not be null");
        this.realmType = realmType;
    }

    @Override
    public AccessContext extract(HttpServletRequest request) {
        String realmId = request.getHeader(headerName);
        if (!StringUtils.hasLength(realmId))
            return null;
        return new AccessContext(Long.parseLong(realmId), realmType);
    }
}
