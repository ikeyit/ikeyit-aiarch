package com.ikeyit.account.interfaces.api.config;

import com.ikeyit.account.interfaces.api.controller.AccountExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.time.Duration;

@Configuration
public class AccountWebConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(AccountWebConfig.class);

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(AccountApiProperties.API_BASE_URL,
            HandlerTypePredicate.forBasePackageClass(AccountExceptionHandler.class));
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver("locale") {
            @Override
            public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
                String domainName = request.getServerName();
                int pos = domainName.indexOf('.');
                if (pos >= 0) {
                    domainName = domainName.substring(pos);
                }
                log.debug("Parent domain: {}", domainName);
                this.setCookieDomain(domainName);
                super.setLocaleContext(request, response, localeContext);
            }
        };
        resolver.setCookieMaxAge(Duration.ofDays(30));
        resolver.setCookieHttpOnly(false);
        resolver.setCookieSecure(false);
        return resolver;
    }
}
