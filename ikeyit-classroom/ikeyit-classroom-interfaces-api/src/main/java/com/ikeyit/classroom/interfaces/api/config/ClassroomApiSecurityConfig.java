package com.ikeyit.classroom.interfaces.api.config;


import com.ikeyit.security.resource.ResourceSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static com.ikeyit.classroom.interfaces.api.config.ClassroomApiProperties.BASE_URL;


@Configuration(proxyBeanMethods = false)
public class ClassroomApiSecurityConfig {
    @Bean
    public SecurityFilterChain classroomApiSecurityFilterChain(HttpSecurity http,
                                                         ResourceSecurityConfigurer resourceSecurityConfigurer) throws Exception {
        resourceSecurityConfigurer.configure(http)
            .securityMatcher(BASE_URL + "/**")
            .authorizeHttpRequests(c -> c
                .anyRequest()
                .authenticated());
        return http.build();
    }
}