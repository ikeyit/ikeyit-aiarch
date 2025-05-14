package com.ikeyit.access.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.PathContainer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class UrlAccessContextExtractor implements AccessContextExtractor {

    private String realmType;

    private PathPatternParser pathPatternParser = PathPatternParser.defaultInstance;

    private PathPattern pathPattern;

    public UrlAccessContextExtractor() {
    }

    public UrlAccessContextExtractor(String pathPattern, String realmType) {
        Assert.notNull(pathPattern, "pathPattern must not be null");
        Assert.notNull(realmType, "realmType must not be null");
        this.pathPattern = pathPatternParser.parse(pathPattern);
        this.realmType = realmType;

    }

    public void setPathPatternParser(PathPatternParser pathPatternParser) {
        Assert.notNull(pathPatternParser, "pathPatternParser must not be null");
        this.pathPatternParser = pathPatternParser;
    }

    public void setRealmType(String realmType) {
        Assert.notNull(realmType, "realmType must not be null");
        this.realmType = realmType;
    }

    @Override
    public AccessContext extract(HttpServletRequest request) {
        var pathContainer = PathContainer.parsePath(request.getRequestURI());
        var result = pathPattern.matchAndExtract(pathContainer);
        if (result == null)
            return null;
        String realmId = result.getUriVariables().get("realmId");
        if (!StringUtils.hasLength(realmId))
            return null;
        return new AccessContext(Long.parseLong(realmId), realmType);
    }
}
