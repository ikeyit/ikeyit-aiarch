package com.ikeyit.access.web;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class AccessWebSecurityConfigurer {
    private final AccessDeniedHandler accessDeniedHandler;

    public AccessWebSecurityConfigurer(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    public void configure(HttpSecurity http, String pathPattern, String realmType) {
        AccessContextFilter accessContextFilter = new AccessContextFilter();
        accessContextFilter.setAccessDeniedHandler(accessDeniedHandler);
        accessContextFilter.setAccessContextExtractor(new UrlAccessContextExtractor(pathPattern, realmType));
        http.addFilterBefore(accessContextFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }

    public void configure(HttpSecurity http, AccessContextExtractor accessContextExtractor) {
        AccessContextFilter accessContextFilter = new AccessContextFilter();
        accessContextFilter.setAccessDeniedHandler(accessDeniedHandler);
        accessContextFilter.setAccessContextExtractor(accessContextExtractor);
        http.addFilterBefore(accessContextFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
