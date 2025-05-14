package com.ikeyit.access.web;

import com.ikeyit.access.core.AccessService;
import com.ikeyit.access.remote.RemoteAccessService;
import com.ikeyit.security.resource.JsonAccessDeniedHandler;
import com.ikeyit.security.resource.ResourceSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ResourceSecurityAutoConfiguration.class)
public class AccessWebSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AccessService accessService() {
        // TODO cache
        return new RemoteAccessService();
    }


    @Bean
    @ConditionalOnMissingBean
    public AccessJwtAuthenticationConverter accessJwtAuthenticationConverter(AccessService accessService) {
        return new AccessJwtAuthenticationConverter(accessService);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonAccessDeniedHandler jsonAccessDeniedHandler() {
        return new JsonAccessDeniedHandler();
    }

    @Bean
    public AccessWebSecurityConfigurer accessWebConfigurer(JsonAccessDeniedHandler jsonAccessDeniedHandler) {
        return new AccessWebSecurityConfigurer(jsonAccessDeniedHandler);
    }
}
