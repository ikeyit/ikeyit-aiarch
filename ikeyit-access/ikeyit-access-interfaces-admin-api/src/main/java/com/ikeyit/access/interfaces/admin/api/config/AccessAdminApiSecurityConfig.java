package com.ikeyit.access.interfaces.admin.api.config;

import com.ikeyit.access.application.service.AccessConstant;
import com.ikeyit.access.web.AccessContext;
import com.ikeyit.access.web.AccessJwtAuthenticationConverter;
import com.ikeyit.access.web.AccessWebSecurityConfigurer;
import com.ikeyit.security.resource.ResourceSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.ikeyit.access.interfaces.admin.api.config.AccessAdminApiProperties.BASE_URL;

@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity
public class AccessAdminApiSecurityConfig {

    @Bean
    public SecurityFilterChain adminAccessSecurityFilterChain(HttpSecurity http,
                                                                 AccessJwtAuthenticationConverter accessJwtAuthenticationConverter,
                                                                 AccessWebSecurityConfigurer accessWebSecurityConfigurer,
                                                                 ResourceSecurityConfigurer resourceSecurityConfigurer) throws Exception {
        accessWebSecurityConfigurer.configure(http, request -> new AccessContext(0L, AccessConstant.MASTER_REALM_TYPE));
        return resourceSecurityConfigurer.configure(http, accessJwtAuthenticationConverter)
            .securityMatcher(new AntPathRequestMatcher(BASE_URL + "/**"))
            .authorizeHttpRequests(c -> c
                .requestMatchers(BASE_URL + "/member-invitations/**")
                .authenticated()
                .anyRequest()
                .hasAuthority("MEMBER"))
            .build();
    }
}