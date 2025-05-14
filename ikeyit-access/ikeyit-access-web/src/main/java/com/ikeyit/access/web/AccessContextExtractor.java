package com.ikeyit.access.web;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface AccessContextExtractor {
    AccessContext extract(HttpServletRequest request);
}
