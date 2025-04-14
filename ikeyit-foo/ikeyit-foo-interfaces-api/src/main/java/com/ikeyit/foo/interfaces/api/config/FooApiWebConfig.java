package com.ikeyit.foo.interfaces.api.config;

import com.ikeyit.common.web.exception.EnableRestErrorController;
import com.ikeyit.foo.interfaces.api.controller.FooExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 * === AI-NOTE ===
 * Add @EnableRestErrorController to provide centralized error handling for REST endpoints.
 * Use PathMatchConfigurer to add a global path prefix for all request mappings.
 * === AI-NOTE-END ===
 * </pre>
 * Configure mvc related properties
 */
@Configuration(proxyBeanMethods = false)
@EnableRestErrorController
public class FooApiWebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api/foo/v1",
            HandlerTypePredicate.forBasePackageClass(FooExceptionHandler.class));
    }
}