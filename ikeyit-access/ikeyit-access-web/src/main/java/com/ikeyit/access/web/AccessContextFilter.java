package com.ikeyit.access.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AccessContextFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessContextFilter.class);

    private AccessContextExtractor accessContextExtractor = new DefaultAccessContextExtractor();

    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    public AccessContextFilter() {
    }

    public void setAccessContextExtractor(AccessContextExtractor accessContextExtractor) {
        Assert.notNull(accessContextExtractor, "accessContextExtractor must not be null");
        this.accessContextExtractor = accessContextExtractor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            AccessContext accessContext = accessContextExtractor.extract(request);
            if (accessContext != null) {
                log.debug("Resolved AccessContext: {}", accessContext);
                AccessContextHolder.setContext(accessContext);
            } else {
                log.info("Invalid request. AccessContext is missed!");
                AccessDeniedException exception = new AccessDeniedException("AccessContext is missed!");
                this.accessDeniedHandler.handle(request, response, exception);
                return;
            }
            filterChain.doFilter(request, response);
        } finally {
            AccessContextHolder.clearContext();
        }
    }

    public AccessContextFilter setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
        Assert.notNull(accessDeniedHandler, "accessDeniedHandler cannot be null");
        this.accessDeniedHandler = accessDeniedHandler;
        return this;
    }
}
