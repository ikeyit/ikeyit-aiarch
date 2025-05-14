package com.ikeyit.access.interfaces.admin.api.config;

import com.ikeyit.access.interfaces.admin.api.controller.AdminAccessController;
import com.ikeyit.common.web.exception.EnableRestErrorController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.ikeyit.access.interfaces.admin.api.config.AccessAdminApiProperties.BASE_URL;

@Configuration
@EnableRestErrorController
public class AccessAdminApiWebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {;
        configurer.addPathPrefix(BASE_URL, HandlerTypePredicate.forBasePackageClass(AdminAccessController.class));
    }
}

