package com.ikeyit.classroom.interfaces.api.config;

import com.ikeyit.classroom.interfaces.api.controller.ClassroomExceptionHandler;
import com.ikeyit.common.web.exception.EnableRestErrorController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableRestErrorController
public class ClassroomApiWebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(ClassroomApiProperties.BASE_URL, HandlerTypePredicate.forBasePackageClass(ClassroomExceptionHandler.class));
    }
}