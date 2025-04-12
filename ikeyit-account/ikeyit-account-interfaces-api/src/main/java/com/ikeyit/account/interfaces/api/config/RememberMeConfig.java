package com.ikeyit.account.interfaces.api.config;

import com.ikeyit.account.application.service.UserService;
import com.ikeyit.account.interfaces.api.auth.rememberme.CustomRememberMeServices;
import com.ikeyit.account.interfaces.api.auth.rememberme.RememberMeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RememberMeProperties.class)
@ConditionalOnProperty(
    name = "spring.security.remember-me.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class RememberMeConfig {

    @Bean
    public CustomRememberMeServices rememberMeServices(RememberMeProperties rememberMeProperties,
                                                       UserService userService) {
        var rememberMeServices = new CustomRememberMeServices(
            rememberMeProperties.getKey(),
            userService);
        rememberMeServices.setParameter(rememberMeProperties.getParameter());
        rememberMeServices.setCookieName(rememberMeProperties.getCookieName());
        rememberMeServices.setTokenValiditySeconds((int) rememberMeProperties.getTokenValiditySeconds().toSeconds());
        return rememberMeServices;
    }
}
