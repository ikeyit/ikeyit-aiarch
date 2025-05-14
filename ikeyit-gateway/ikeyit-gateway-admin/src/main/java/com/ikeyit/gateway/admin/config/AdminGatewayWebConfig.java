package com.ikeyit.gateway.admin.config;


import com.ikeyit.gateway.admin.controller.GlobalExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.i18n.LocaleContextResolver;

@Configuration(proxyBeanMethods = false)
public class AdminGatewayWebConfig {

    @Bean
    @Order(-1) // Place it before WebFluxResponseStatusExceptionHandler to capture ResponseStatusException
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public LocaleContextResolver localeContextResolver() {
        return new CookieLocaleResolver();
    }
}
